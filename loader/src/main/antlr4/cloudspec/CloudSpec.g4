grammar CloudSpec;

spec: specDecl groupDecl+;

specDecl: SPEC STRING;

groupDecl: GROUP STRING ruleDecl+;

ruleDecl: RULE STRING onDecl withDecl? assertDecl;

onDecl: ON RESOURCE_DEF_REF;

withDecl: WITH statement andDecl*;

assertDecl: ASSERT statement andDecl*;

andDecl: AND statement;

predicate: IS? ('==' | EQUAL TO) value      # PropertyEqualPredicate
         | IS? ('!=' | NOT EQUAL TO) value  # PropertyNotEqualPredicate
         | IS? WITHIN array                 # PropertyWithinPredicate
         | IS? NOT WITHIN array             # PropertyNotWithinPredicate
         ;

statement: MEMBER_NAME predicate                              # PropertyStatement
         | MEMBER_NAME '[' STRING ']' predicate               # KeyValuePropertyStatement
         | MEMBER_NAME '('  statement (',' statement)* ')'    # NestedPropertyStatement
         | '>' MEMBER_NAME '(' statement (',' statement)* ')' # AssociationStatement
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
EQUAL: [Ee][Qq][Uu][Aa][Ll];
ENABLED: [Ee][Nn][Aa][Bb][Ll][Ee][Dd];
DISABLED: [Dd][Ii][Ss][Aa][Bb][Ll][Ee][Dd];
TRUE: [Tt][Rr][Uu][Ee];
FALSE: [Ff][Aa][Ll][Ss][Ee];
HAS: [Hh][Aa][Ss];
BEFORE: [Bb][Ee][Ff][Oo][Rr][Ee];
AFTER: [Aa][Ff][Tt][Ee][Rr];
AT: [Aa][Tt];
NOT: [Nn][Oo][Tt];
TO: [Tt][Oo];
LESS_THAN: LESS THAN;
GREATER_THAN: GREATER THAN;
LESS: [Ll][Ee][Ss][Ss];
GREATER: [Gg][Rr][Ee][Aa][Tt][Ee][Rr];
THAN: [Tt][Hh][Aa][Nn];

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
MEMBER_NAME: LETTER ALPHANUM*;

WS: [ \t\r\n]+ -> skip;
SL_COMMENT: '//' .*? '\n' -> skip;