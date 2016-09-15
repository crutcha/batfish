package org.batfish.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.batfish.common.BatfishException;
import org.batfish.datamodel.ForwardingAction;
import org.batfish.datamodel.Ip;
import org.batfish.datamodel.IpProtocol;
import org.batfish.datamodel.IpWildcard;
import org.batfish.datamodel.SubRange;
import org.batfish.datamodel.questions.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;

public class QuestionHelper {

   public enum MacroType {
      CHECKPROTECTION("checkprotection"),
      CHECKREACHABILITY("checkreachability"),
      TRACEROUTE("traceroute");

      private final static Map<String, MacroType> _map = buildMap();

      private static Map<String, MacroType> buildMap() {
         Map<String, MacroType> map = new HashMap<>();
         for (MacroType value : MacroType.values()) {
            String name = value._name;
            map.put(name, value);
         }
         return Collections.unmodifiableMap(map);
      }

      @JsonCreator
      public static MacroType fromName(String name) {
         MacroType instance = _map.get(name.toLowerCase());
         if (instance == null) {
            throw new BatfishException(
                  "Not a valid MacroType: \"" + name + "\"");
         }
         return instance;
      }

      private final String _name;

      private MacroType(String name) {
         _name = name;
      }

      public String macroTypeName() {
         return _name;
      }
   }

   public static final String MACRO_PREFIX = "#";

   public static String getParametersString(Map<String, String> parameters)
         throws Exception {
      String retString = "{\n";

      for (String paramKey : parameters.keySet()) {
         retString += String.format("\"%s\" : %s,\n", paramKey,
               parameters.get(paramKey));
      }

      retString += "}\n";

      return retString;
   }

   public static Question getQuestion(QuestionType questionType) {

      switch (questionType) {
      case ACL_REACHABILITY:
         return new AclReachabilityQuestion();
      case BGP_ADVERTISEMENTS:
         return new BgpAdvertisementsQuestion();
      case BGP_SESSION_CHECK:
         return new BgpSessionCheckQuestion();
      case COMPARE_SAME_NAME:
         return new CompareSameNameQuestion();
      case ENVIRONMENT_CREATION:
         return new EnvironmentCreationQuestion();
      case ERROR:
         return new ErrorQuestion();
      case IPSEC_VPN_CHECK:
         return new IpsecVpnCheckQuestion();
      case ISIS_LOOPBACKS:
         return new IsisLoopbacksQuestion();
      case NEIGHBORS:
         return new NeighborsQuestion();
      case NODES:
         return new NodesQuestion();
      case OSPF_LOOPBACKS:
         return new OspfLoopbacksQuestion();
      case PAIRWISE_VPN_CONNECTIVITY:
         return new PairwiseVpnConnectivityQuestion();
      case PROTOCOL_DEPENDENCIES:
         return new ProtocolDependenciesQuestion();
      case REACHABILITY:
         return new ReachabilityQuestion();
      case SELF_ADJACENCIES:
         return new SelfAdjacenciesQuestion();
      case TRACEROUTE:
         return new TracerouteQuestion();
      case UNDEFINED_REFERENCES:
         return new UndefinedReferencesQuestion();
      case UNIQUE_BGP_PREFIX_ORIGINATION:
         return new UniqueBgpPrefixOriginationQuestion();
      case UNIQUE_IP_ASSIGNMENTS:
         return new UniqueIpAssignmentsQuestion();
      case UNUSED_STRUCTURES:
         return new UnusedStructuresQuestion();
      default:
         break;
      }

      throw new BatfishException("Unsupported question type " + questionType);
   }

   public static Question getQuestion(String questionTypeStr,
         Map<String, Supplier<Question>> questions) {
      QuestionType qType = QuestionType.fromName(questionTypeStr);
      Question question;
      if (qType != null) {
         question = getQuestion(qType);
      }
      else {
         Supplier<Question> supplier = questions.get(questionTypeStr);
         if (supplier == null) {
            throw new BatfishException(
                  "No question of type: " + questionTypeStr);
         }
         question = supplier.get();
      }
      return question;
   }

   public static String getQuestionString(QuestionType questionType)
         throws JsonProcessingException {
      return getQuestion(questionType).toJsonString();
   }

   public static String getQuestionString(String questionTypeStr,
         Map<String, Supplier<Question>> questions)
         throws JsonProcessingException {
      Question question = getQuestion(questionTypeStr, questions);
      return question.toJsonString();
   }

   public static ReachabilityQuestion getReachabilityQuestion(String dstIp,
         String protocol, String ingressNodeRegex, ForwardingAction action) {
      ReachabilityQuestion question = new ReachabilityQuestion();

      question.setDstIps(Collections.singleton(new IpWildcard(new Ip(dstIp))));

      boolean inverted = false;

      if (protocol.startsWith("!")) {
         inverted = true;
         protocol = protocol.replace("!", "");
      }

      MyApplication application = new MyApplication(protocol);

      IpProtocol ipProtocol = application.getIpProtocol();
      if (inverted) {
         question.setNotIpProtocols(Collections.singleton(ipProtocol));
      }
      else {
         question.setIpProtocols(Collections.singleton(ipProtocol));
      }

      if (application.getPort() != null) {
         int portNum = application.getPort();
         Set<SubRange> portRanges = Collections
               .singleton(new SubRange(portNum));
         if (inverted) {
            question.setNotDstPorts(portRanges);
         }
         else {
            question.setDstPorts(portRanges);
         }
      }

      if (ingressNodeRegex != null) {
         question.setIngressNodeRegex(ingressNodeRegex);
      }

      Set<ForwardingAction> actionSet = new HashSet<>();
      actionSet.add(action);
      question.setActions(actionSet);

      return question;
   }

   public static String resolveMacro(String macroName, String paramsLine)
         throws JsonProcessingException {
      String macro = macroName.replace(MACRO_PREFIX, "");
      MacroType macroType = MacroType.fromName(macro);

      switch (macroType) {
      case CHECKPROTECTION: {
         String[] words = paramsLine.split(" ");
         if (words.length < 2 || words.length > 3) {
            throw new BatfishException(
                  "Incorrect usage for noreachability macro. "
                        + "Should be:\n #checkreachability <dstip> <protocol> [<ingressNodeRegex>]");
         }
         String dstIp = words[0];
         String protocol = words[1];
         String ingressNodeRegex = (words.length == 3) ? words[2] : null;

         return getReachabilityQuestion(dstIp, protocol, ingressNodeRegex,
               ForwardingAction.ACCEPT).toJsonString();
      }
      case CHECKREACHABILITY: {
         String[] words = paramsLine.split(" ");
         if (words.length < 2 || words.length > 3) {
            throw new BatfishException(
                  "Incorrect usage for noreachability macro. "
                        + "Should be:\n #checkreachability <dstip> <protocol> [<ingressNodeRegex>]");
         }
         String dstIp = words[0];
         String protocol = words[1];
         String ingressNodeRegex = (words.length == 3) ? words[2] : null;

         return getReachabilityQuestion(dstIp, protocol, ingressNodeRegex,
               ForwardingAction.DROP).toJsonString();
      }
      case TRACEROUTE: {
         String[] words = paramsLine.split(" ");
         if (words.length < 2 || words.length > 3) {
            throw new BatfishException("Incorrect usage for traceroute macro. "
                  + "Should be:\n #traceroute <srcNode> <dstip> [<protocol>]");
         }
         TracerouteQuestion question = new TracerouteQuestion();
         String srcNode = words[0];
         String dstIp = words[1];

         question.setIngressNode(srcNode);
         question.setDstIp(new Ip(dstIp));

         if (words.length == 3) {
            String protocol = words[2];
            MyApplication application = new MyApplication(protocol);
            question.setIpProtocol(application.getIpProtocol());
            if (application.getPort() != null) {
               question.setDstPort(application.getPort());
            }
         }
         // else {
         // question.setIpProtocol(IpProtocol.ICMP);
         // }
         return question.toJsonString();
      }
      default:
         throw new BatfishException("Unknown macrotype: " + macroType);
      }
   }
}
