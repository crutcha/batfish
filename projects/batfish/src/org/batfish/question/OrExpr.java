package org.batfish.question;

import java.util.HashSet;
import java.util.Set;

public class OrExpr implements BooleanExpr {

   private final Set<BooleanExpr> _disjuncts;

   public OrExpr(Set<BooleanExpr> disjuncts) {
      _disjuncts = new HashSet<BooleanExpr>();
      _disjuncts.addAll(disjuncts);
   }

   @Override
   public boolean evaluate(AssertionCtx context) {
      for (BooleanExpr disjunct : _disjuncts) {
         if (disjunct.evaluate(context)) {
            return true;
         }
      }
      return false;
   }

}
