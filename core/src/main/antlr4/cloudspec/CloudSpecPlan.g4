grammar CloudSpecPlan;

import CloudSpecCommon;

plan: planDecl;

planDecl: PLAN STRING setDecl* useModuleDecl+;

useModuleDecl: USE MODULE STRING;