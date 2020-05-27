grammar CloudSpec;

spec: specDecl groupDecl+;

specDecl: SPEC STRING;

groupDecl: GROUP STRING ruleDecl+;

ruleDecl: RULE STRING onDecl withDecl? assertDecl;

onDecl: ON RESOURCE_DEF_REF;

withDecl: WITH statement andDecl*;

assertDecl: ASSERT statement andDecl*;

andDecl: AND statement;

predicate: IS_NULL                        # ValueNullPredicate
         | IS_NOT_NULL                    # ValueNotNullPredicate
         | IS_EQUAL_TO value              # ValueEqualPredicate
         | IS_NOT_EQUAL_TO value          # ValueNotEqualPredicate
         | IS_WITHIN array                # ValueWithinPredicate
         | IS_NOT_WITHIN array            # ValueNotWithinPredicate
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
         ;

statement: MEMBER_NAME predicate                      # PropertyStatement
         | MEMBER_NAME '[' STRING ']' predicate       # KeyValuePropertyStatement
         | MEMBER_NAME '('  statement andDecl* ')'    # NestedPropertyStatement
         | '>'MEMBER_NAME '(' statement andDecl* ')'  # AssociationStatement
         ;

stringValue: STRING;
numberValue: (INTEGER | DOUBLE);
booleanValue: BOOLEAN;

value: numberValue
     | stringValue
     | booleanValue
     ;

array: '[' value (',' value)* ']';

// Values
STRING: '"' ('\\"'|.)*? '"';
BOOLEAN: (TRUE | FALSE);
DOUBLE: NEGATIVE? DIGIT+ DOT DIGIT+;
INTEGER: NEGATIVE? DIGIT+;
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

// Vocabulary
SPEC: [Ss][Pp][Ee][Cc];
GROUP: [Gg][Rr][Oo][Uu][Pp];
RULE: [Rr][Uu][Ll][Ee];
ON: [Oo][Nn];
WITH: [Ww][Ii][Tt][Hh];
ASSERT: [Aa][Ss][Ss][Ee][Rr][Tt];
AND: [Aa][Nn][Dd];

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
RESOURCE_DEF_REF: PROVIDER_NAMESPACE (':' GROUP_NAMESPACE)? ':' RESOURCE_TYPE;
fragment PROVIDER_NAMESPACE: LETTER ALPHANUM*;
fragment GROUP_NAMESPACE: LETTER ALPHANUM*;
fragment RESOURCE_TYPE: LETTER ALPHANUM*;
MEMBER_NAME: LETTER [a-zA-Z0-9_]*;

WS: [ \t\r\n]+ -> skip;
SL_COMMENT: '//' .*? '\n' -> skip;