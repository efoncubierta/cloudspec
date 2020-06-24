grammar CloudSpec;

import CloudSpecLex;

planDecl: PLAN STRING inputDecl* setDecl*
          (useModuleDecl | useGroupDecl | useRuleDecl | groupDecl | ruleDecl)+
          END_PLAN;

moduleDecl: MODULE STRING inputDecl* setDecl*
            (useModuleDecl | useGroupDecl | useRuleDecl | groupDecl | ruleDecl)+
            END_MODULE;

inputDecl: INPUT STRING AS PROPERTY_REF;

setDecl: SET CONFIG_REF EQUAL_SYMBOL configValue;

useModuleDecl: USE_MODULE STRING;

useGroupDecl: USE_GROUP STRING;

useRuleDecl: USE_RULE STRING;

groupDecl: GROUP STRING setDecl* (useRuleDecl | ruleDecl)+ END_GROUP;

ruleDecl: RULE STRING setDecl* onDecl withDecl? assertDecl END_RULE;

onDecl: ON RESOURCE_DEF_REF;

withDecl: WITH statement andDecl*;

assertDecl: ASSERT statement andDecl*;

andDecl: AND statement;

predicate: IS_NULL                              # ValueNullPredicate
         | IS_NOT_NULL                          # ValueNotNullPredicate
         | IS_EQUAL_TO singleValue              # ValueEqualPredicate
         | IS_NOT_EQUAL_TO singleValue          # ValueNotEqualPredicate
         | IS_WITHIN arrayValue                 # ValueWithinPredicate
         | IS_NOT_WITHIN arrayValue             # ValueNotWithinPredicate
         // number predicates
         | IS_LESS_THAN numberValue                # NumberLessThanPredicate
         | IS_LESS_THAN_EQUAL numberValue          # NumberLessThanEqualPredicate
         | IS_GREATER_THAN numberValue             # NumberGreaterThanPredicate
         | IS_GREATER_THAN_EQUAL numberValue       # NumberGreaterThanEqualPredicate
         | IS_BETWEEN numberValue AND numberValue  # NumberBetweenPredicate
         // string predicates
         | IS_EMPTY                             # StringEmptyPredicate
         | IS_NOT_EMPTY                         # StringNotEmptyPredicate
         | IS_STARTING_WITH stringValue         # StringStartingWithPredicate
         | IS_NOT_STARTING_WITH stringValue     # StringNotStartingWithPredicate
         | IS_ENDING_WITH stringValue           # StringEndingWithPredicate
         | IS_NOT_ENDING_WITH stringValue       # StringNotEndingWithPredicate
         | IS_CONTAINING stringValue            # StringContainingPredicate
         | IS_NOT_CONTAINING stringValue        # StringNotContainingPredicate
         // ip addresses predicates
         | IS_EQUAL_TO_IP_ADDRESS stringValue             # IpAddressEqualPredicate
         | IS_NOT_EQUAL_TO_IP_ADDRESS stringValue         # IpAddressNotEqualPredicate
         | IS_LESS_THAN_IP_ADDRESS stringValue            # IpAddressLessThanPredicate
         | IS_LESS_THAN_EQUAL_IP_ADDRESS stringValue      # IpAddressLessThanEqualPredicate
         | IS_GREATER_THAN_IP_ADDRESS stringValue         # IpAddressGreaterThanPredicate
         | IS_GREATER_THAN_EQUAL_IP_ADDRESS stringValue   # IpAddressGreaterThanEqualPredicate
         | IS_WITHIN_NETWORK stringValue                  # IpWithinNetworkPredicate
         | IS_NOT_WITHIN_NETWORK stringValue              # IpNotWithinNetworkPredicate
         | IS_IPV4                                        # IpIsIpv4Predicate
         | IS_IPV6                                        # IpIsIpv6Predicate
         // boolean predicates
         | IS_ENABLED                               # EnabledPredicate
         | IS_DISABLED                              # DisabledPredicate
         // date predicates
         | IS_BEFORE dateValue                      # DateBeforePredicate
         | IS_NOT_BEFORE dateValue                  # DateNotBeforePredicate
         | IS_AFTER dateValue                       # DateAfterPredicate
         | IS_NOT_AFTER dateValue                   # DateNotAfterPredicate
         | IS_BETWEEN dateValue AND dateValue       # DateBetweenPredicate
         ;

statement: MEMBER_NAME predicate                      # PropertyStatement
         | MEMBER_NAME '[' STRING ']' predicate       # KeyValuePropertyStatement
         | MEMBER_NAME '('  statement andDecl* ')'    # NestedPropertyStatement
         | '>'MEMBER_NAME '(' statement andDecl* ')'  # AssociationStatement
         ;

stringValue: STRING;
numberValue: (INTEGER | DOUBLE);
booleanValue: BOOLEAN;
dateValue: DATE_STRING;

singleValue: numberValue
           | stringValue
           | booleanValue
           | dateValue
           ;

singleConfigValue: numberValue
                 | stringValue
                 | booleanValue
                 ;

arrayValue: '[' singleValue (',' singleValue)* ']';

arrayConfigValue: '[' singleConfigValue (',' singleConfigValue)* ']';

configValue: singleConfigValue | arrayConfigValue;