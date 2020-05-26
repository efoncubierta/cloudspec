grammar CloudSpec;

spec: specDecl groupDecl+;

specDecl: SPEC STRING;

groupDecl: GROUP STRING ruleDecl+;

ruleDecl: RULE STRING onDecl withDecl? assertDecl;

onDecl: ON RESOURCE_DEF_REF;

withDecl: WITH statement andDecl*;

assertDecl: ASSERT statement andDecl*;

andDecl: AND statement;

predicate: IS? ('==' | EQUAL_TO) value                      # ValueEqualPredicate
         | IS? ('!=' | NOT EQUAL_TO) value                  # ValueNotEqualPredicate
         | IS? WITHIN array                                 # ValueWithinPredicate
         | IS? NOT WITHIN array                             # ValueNotWithinPredicate
         // number predicates
         | IS? ('<' | LESS_THAN) value                      # NumberLessThanPredicate
         | IS? ('<=' | LESS_THAN_EQUAL) value               # NumberLessThanEqualPredicate
         | IS? ('>' | GREATER_THAN) value                   # NumberGreaterThanPredicate
         | IS? ('>=' | GREATER_THAN_EQUAL) value            # NumberGreaterThanEqualPredicate
         | IS? BETWEEN value AND value                      # NumberBetweenPredicate
         // string predicates
         | STARTING_WITH value                              # StringStartingWithPredicate
         | NOT_STARTING_WITH value                          # StringNotStartingWithPredicate
         | ENDING_WITH value                                # StringEndingWithPredicate
         | NOT_ENDING_WITH value                            # StringNotEndingWithPredicate
         | CONTAINING value                                 # StringContainingPredicate
         | NOT_CONTAINING value                             # StringNotContainingPredicate
         // ip addresses predicates
         | IS? ('==' | EQUAL_TO) IP_ADDRESS value           # IpAddressEqualPredicate
         | IS? ('!=' | NOT EQUAL_TO) IP_ADDRESS value       # IpAddressNotEqualPredicate
         | IS? ('<'  | LESS_THAN) IP_ADDRESS value          # IpAddressLessThanPredicate
         | IS? ('<=' | LESS_THAN_EQUAL) IP_ADDRESS value    # IpAddressLessThanEqualPredicate
         | IS? ('>'  | GREATER_THAN) IP_ADDRESS value       # IpAddressGreaterThanPredicate
         | IS? ('>=' | GREATER_THAN_EQUAL) IP_ADDRESS value # IpAddressGreaterThanEqualPredicate
         | IS? WITHIN NETWORK value                         # IpWithinNetworkPredicate
         | IS? NOT WITHIN NETWORK value                     # IpNotWithinNetworkPredicate
         // boolean predicates
         | IS? ENABLED                                      # EnabledPredicate
         | IS? DISABLED                                     # DisabledPredicate
         ;

statement: MEMBER_NAME predicate                      # PropertyStatement
         | MEMBER_NAME '[' STRING ']' predicate       # KeyValuePropertyStatement
         | MEMBER_NAME '('  statement andDecl* ')'    # NestedPropertyStatement
         | '>' MEMBER_NAME '(' statement andDecl* ')' # AssociationStatement
         ;

value: STRING                    # StringValue
     | BOOLEAN                   # BooleanValue
     | INTEGER                   # IntegerValue
     | DOUBLE                    # DoubleValue
     ;

array: '[' value (',' value)* ']';

// Values
STRING: '"' ('\\"'|.)*? '"';
BOOLEAN: (ENABLED | DISABLED | TRUE | FALSE);
DOUBLE: NEGATIVE? DIGIT+ DOT DIGIT+;
INTEGER: NEGATIVE? DIGIT+;
fragment DIGIT: [0-9];
fragment DOT: '.';
fragment NEGATIVE: '-';

// Vocabulary
LESS_THAN: (LESS ' ' THAN | LT);
GREATER_THAN: (GREATER ' ' THAN | GT);
LESS_THAN_EQUAL: (LESS ' ' THAN ' ' OR ' ' EQUAL ' ' TO | LTE);
GREATER_THAN_EQUAL: (GREATER ' ' THAN ' ' OR ' ' EQUAL ' ' TO | LTE);
STARTING_WITH: STARTING ' ' WITH;
NOT_STARTING_WITH: STARTING ' ' WITH;
fragment STARTING: [Ss][Tt][Aa][Rr][Tt][Ii][Nn][Gg];
ENDING_WITH: ENDING ' ' WITH;
NOT_ENDING_WITH: ENDING ' ' WITH;
fragment ENDING: [Ee][Nn][Dd][Ii][Nn][Gg];
NOT_CONTAINING: NOT ' ' CONTAINING;
CONTAINING: [Cc][Oo][Nn][Tt][Aa][Ii][Nn][Gg];
SPEC: [Ss][Pp][Ee][Cc];
GROUP: [Gg][Rr][Oo][Uu][Pp];
RULE: [Rr][Uu][Ll][Ee];
ON: [Oo][Nn];
WITH: [Ww][Ii][Tt][Hh];
ASSERT: [Aa][Ss][Ss][Ee][Rr][Tt];
WITHIN: [Ww][Ii][Tt][Hh][Ii][Nn];
IS: [Ii][Ss];
AND: [Aa][Nn][Dd];
OR: [Oo][Rr];
EQUAL_TO: EQUAL ' ' TO;
fragment EQUAL: [Ee][Qq][Uu][Aa][Ll];
ENABLED: [Ee][Nn][Aa][Bb][Ll][Ee][Dd];
DISABLED: [Dd][Ii][Ss][Aa][Bb][Ll][Ee][Dd];
TRUE: [Tt][Rr][Uu][Ee];
FALSE: [Ff][Aa][Ll][Ss][Ee];
NOT: [Nn][Oo][Tt];
fragment TO: [Tt][Oo];
fragment GT: [Gg][Tt];
fragment GTE: [Gg][Tt][Ee];
fragment LT: [Ll][Tt];
fragment LTE: [Ll][Tt][Ee];
fragment LESS: [Ll][Ee][Ss][Ss];
fragment GREATER: [Gg][Rr][Ee][Aa][Tt][Ee][Rr];
fragment THAN: [Tt][Hh][Aa][Nn];
BETWEEN: [Bb][Ee][Tt][Ww][Ee][Ee][Nn];
IP_ADDRESS: IP ' ' ADDRESS?;
NETWORK: [Nn][Ee][Tt][Ww][Oo][Rr][Kk];
fragment ADDRESS: [Aa][Dd][Dd][Rr][Ee][Ss];
fragment CIDR: [Cc][Ii][Dd][Rr];
fragment IP: [Ii][Pp];

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