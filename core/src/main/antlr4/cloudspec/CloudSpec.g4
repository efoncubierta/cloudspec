grammar CloudSpec;

spec: setDecl* moduleDecl+;

setDecl: SET CONFIG_REF EQUAL_SYMBOL configValue;

moduleDecl: MODULE STRING setDecl* groupDecl+;

groupDecl: GROUP STRING setDecl* ruleDecl+;

ruleDecl: RULE STRING setDecl* onDecl withDecl? assertDecl;

onDecl: ON RESOURCE_DEF_REF;

withDecl: WITH statement andDecl*;

assertDecl: ASSERT statement andDecl*;

andDecl: AND statement;

predicate: IS_NULL                        # ValueNullPredicate
         | IS_NOT_NULL                    # ValueNotNullPredicate
         | IS_EQUAL_TO singleValue              # ValueEqualPredicate
         | IS_NOT_EQUAL_TO singleValue          # ValueNotEqualPredicate
         | IS_WITHIN arrayValue                # ValueWithinPredicate
         | IS_NOT_WITHIN arrayValue            # ValueNotWithinPredicate
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

// Date value
DATE_STRING: '"' DATE_FORMAT (' ' TIME_FORMAT)? '"';
STRING: '"' ('\\"'|.)*? '"';
BOOLEAN: (TRUE | FALSE);
DOUBLE: NEGATIVE? DIGIT+ DOT DIGIT+;
INTEGER: NEGATIVE? DIGIT+;
fragment DATE_FORMAT: YEAR_FORMAT '-' MONTH_FORMAT '-' DAY_FORMAT;
fragment TIME_FORMAT: HOUR_FORMAT ':' MINUTE_FORMAT ':' SECOND_FORMAT;
fragment YEAR_FORMAT: DIGIT DIGIT DIGIT DIGIT;
fragment MONTH_FORMAT: DIGIT DIGIT;
fragment DAY_FORMAT: DIGIT DIGIT;
fragment HOUR_FORMAT: DIGIT DIGIT;
fragment MINUTE_FORMAT: DIGIT DIGIT;
fragment SECOND_FORMAT: DIGIT DIGIT;
fragment DIGIT: [0-9];
fragment DOT: '.';
fragment NEGATIVE: '-';

// Predicates
IS_NULL:               (IS ' ')? NULL;
IS_NOT_NULL:           (IS ' ')? NOT ' ' NULL;
IS_EQUAL_TO:           (IS ' ')? ('==' | EQUAL ' ' TO);
IS_NOT_EQUAL_TO:       (IS ' ')? ('!=' | NOT ' ' EQUAL ' ' TO);
IS_WITHIN:             (IS ' ')? WITHIN;
IS_NOT_WITHIN:         (IS ' ')? NOT ' ' WITHIN;

IS_LESS_THAN:          (IS ' ')? ('<'  | LESS_THAN);
IS_LESS_THAN_EQUAL:    (IS ' ')? ('<=' | LESS_THAN_EQUAL);
IS_GREATER_THAN:       (IS ' ')? ('>'  | GREATER_THAN);
IS_GREATER_THAN_EQUAL: (IS ' ')? ('>=' | GREATER_THAN_EQUAL);
IS_BETWEEN:            (IS ' ')? BETWEEN;

IS_EMPTY:              (IS ' ')? EMPTY;
IS_NOT_EMPTY:          (IS ' ')? NOT ' ' EMPTY;
IS_STARTING_WITH:      (IS ' ')? STARTING ' ' WITH;
IS_NOT_STARTING_WITH:  (IS ' ')? NOT ' ' STARTING ' ' WITH;
IS_ENDING_WITH:        (IS ' ')? ENDING ' ' WITH;
IS_NOT_ENDING_WITH:    (IS ' ')? NOT ' ' ENDING ' ' WITH;
IS_CONTAINING:         (IS ' ')? CONTAINING;
IS_NOT_CONTAINING:     (IS ' ')? NOT ' ' CONTAINING;

IS_EQUAL_TO_IP_ADDRESS:           IS_EQUAL_TO ' ' IP (' ' ADDRESS)?;
IS_NOT_EQUAL_TO_IP_ADDRESS:       IS_NOT_EQUAL_TO ' ' IP (' ' ADDRESS)?;
IS_LESS_THAN_IP_ADDRESS:          IS_LESS_THAN ' ' IP (' ' ADDRESS)?;
IS_LESS_THAN_EQUAL_IP_ADDRESS:    IS_LESS_THAN_EQUAL ' ' IP (' ' ADDRESS)?;
IS_GREATER_THAN_IP_ADDRESS:       IS_GREATER_THAN ' ' IP (' ' ADDRESS)?;
IS_GREATER_THAN_EQUAL_IP_ADDRESS: IS_GREATER_THAN_EQUAL ' ' IP (' ' ADDRESS)?;
IS_WITHIN_NETWORK:                IS_WITHIN ' ' NETWORK (' ' CIDR)?;
IS_NOT_WITHIN_NETWORK:            IS_NOT_WITHIN ' ' NETWORK (' ' CIDR)?;
IS_IPV4:                          (IS ' ')? IPV4;
IS_IPV6:                          (IS ' ')? IPV6;

IS_ENABLED:                       (IS ' ')? (TRUE | ENABLED);
IS_DISABLED:                      (IS ' ')? (FALSE | DISABLED);

IS_BEFORE:                        (IS ' ')? BEFORE;
IS_NOT_BEFORE:                    (IS ' ')? NOT ' ' BEFORE;
IS_AFTER:                         (IS ' ')? AFTER;
IS_NOT_AFTER:                     (IS ' ')? NOT ' ' AFTER;

// Assignment
EQUAL_SYMBOL: '=';

// Vocabulary
MODULE: [Mm][Oo][Dd][Uu][Ll][Ee];
GROUP: [Gg][Rr][Oo][Uu][Pp];
RULE: [Rr][Uu][Ll][Ee];
ON: [Oo][Nn];
WITH: [Ww][Ii][Tt][Hh];
ASSERT: [Aa][Ss][Ss][Ee][Rr][Tt];
AND: [Aa][Nn][Dd];
SET: [Ss][Ee][Tt];

fragment STARTING:   [Ss][Tt][Aa][Rr][Tt][Ii][Nn][Gg];
fragment ENDING:     [Ee][Nn][Dd][Ii][Nn][Gg];
fragment CONTAINING: [Cc][Oo][Nn][Tt][Aa][Ii][Nn][Ii][Nn][Gg];

// Comparators vocabulary
fragment LESS_THAN:          (LESS    ' ' THAN                         | LT);
fragment LESS_THAN_EQUAL:    (LESS    ' ' THAN ' ' OR ' ' EQUAL ' ' TO | LTE);
fragment GREATER_THAN:       (GREATER ' ' THAN                         | GT);
fragment GREATER_THAN_EQUAL: (GREATER ' ' THAN ' ' OR ' ' EQUAL ' ' TO | GTE);
fragment LESS: [Ll][Ee][Ss][Ss];
fragment LT: [Ll][Tt];
fragment LTE: [Ll][Tt][Ee];
fragment GREATER: [Gg][Rr][Ee][Aa][Tt][Ee][Rr];
fragment GT: [Gg][Tt];
fragment GTE: [Gg][Tt][Ee];
fragment THAN: [Tt][Hh][Aa][Nn];
fragment IS: [Ii][Ss];
fragment WITHIN: [Ww][Ii][Tt][Hh][Ii][Nn];
fragment OR: [Oo][Rr];
fragment EQUAL: [Ee][Qq][Uu][Aa][Ll];
fragment ENABLED: [Ee][Nn][Aa][Bb][Ll][Ee][Dd];
fragment DISABLED: [Dd][Ii][Ss][Aa][Bb][Ll][Ee][Dd];
fragment TRUE: [Tt][Rr][Uu][Ee];
fragment FALSE: [Ff][Aa][Ll][Ss][Ee];
fragment NOT: [Nn][Oo][Tt];
fragment TO: [Tt][Oo];
fragment BETWEEN: [Bb][Ee][Tt][Ww][Ee][Ee][Nn];
fragment NULL: [Nn][Uu][Ll][Ll];
fragment EMPTY: [Ee][Mm][Pp][Tt][Yy];
fragment BEFORE: [Bb][Ee][Ff][Oo][Rr][Ee];
fragment AFTER: [Aa][Ff][Tt][Ee][Rr];

// Network vocabulary
fragment IP: [Ii][Pp];
fragment ADDRESS: [Aa][Dd][Dd][Rr][Ee][Ss][Ss];
fragment NETWORK: [Nn][Ee][Tt][Ww][Oo][Rr][Kk];
fragment CIDR: [Cc][Ii][Dd][Rr];
fragment IPV4: [Ii][Pp][Vv][4];
fragment IPV6: [Ii][Pp][Vv][6];

// Values
fragment LETTER: [a-zA-Z];
fragment LETTERS: [a-zA-Z]+;
fragment ALPHANUM: [a-zA-Z0-9];
fragment ALPHANUMS: [a-zA-Z0-9]+;

// Resource and member references
RESOURCE_DEF_REF: PROVIDER_NAME ':' GROUP_NAME ':' RESOURCE_NAME;
CONFIG_REF: PROVIDER_NAME ':' CONFIG_NAME;
fragment PROVIDER_NAME: LETTER [a-zA-Z0-9_]*;
fragment GROUP_NAME: LETTER [a-zA-Z0-9_]*;
fragment RESOURCE_NAME: LETTER [a-zA-Z0-9_]*;
fragment CONFIG_NAME: LETTER [a-zA-Z0-9_]*;
MEMBER_NAME: LETTER [a-zA-Z0-9_]*;

WS: [ \t\r\n]+ -> skip;
SL_COMMENT: '//' .*? '\n' -> skip;