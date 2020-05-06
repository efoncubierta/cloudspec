grammar CloudSpec;

spec: specDecl groupDecl+;

specDecl: SPEC STRING;

groupDecl: GROUP STRING ruleDecl+;

ruleDecl: RULE STRING onDecl assertDecl*;

onDecl: ON RESOURCE_DEF_REF withDecl*;

withDecl: WITH PROPERTY_NAME withPredicate;

assertDecl: ASSERT PROPERTY_NAME (mustPredicate | shouldPredicate);

// Expressions

// With PROPERTY_NAME
//   == B
//   != B
//   EQUAL TO B
//   NOT EQUAL TO B
//   IN [B]
//   NOT IN [B]

// Assert PROPERTY_NAME
//   BE == B
//   BE != B
//   BE EQUAL TO B
//   NOT BE EQUAL TO B
//   BE IN [B]
//   NOT BE IN [B]

withPredicate: IS? ('==' | EQUAL TO) value             # WithEqualPredicate
             | IS? ('!=' | NOT EQUAL TO) value         # WithNotEqualPredicate
             | IS? WITHIN array                        # WithWithinPredicate
             | IS? NOT WITHIN array                    # WithNotWithinPredicate
             ;

assertPredicate: BE ('==' | EQUAL TO) value           # AssertEqualPredicate
               | (BE '!=' | NOT BE EQUAL TO) value    # AssertNotEqualPredicate
               | BE WITHIN array                      # AssertWithinPredicate
               | NOT BE WITHIN array                  # AssertNotWithinPredicate
               ;

mustPredicate: MUST assertPredicate;
shouldPredicate: SHOULD assertPredicate;

value: STRING                    # StringValue
     | BOOLEAN                   # BooleanValue
     | INTEGER                   # IntegerValue
     ;

array: '[' value (',' value)* ']';

// Vocabulary
SPEC: [Ss][Pp][Ee][Cc];
GROUP: [Gg][Rr][Oo][Uu][Pp];
RULE: [Rr][Uu][Ll][Ee];
ON: [Oo][Nn];
WITH: [Ww][Ii][Tt][Hh];
ASSERT: [Aa][Ss][Ss][Ee][Rr][Tt];
WITHIN: [Ww][Ii][Tt][Hh][Ii][Nn];
IS: [Ii][Ss];
EQUAL: [Ee][Qq][Uu][Aa][Ll];
MUST: [Mm][Uu][Ss][Tt];
SHOULD: [Ss][Hh][Oo][Uu][Ll][Dd];
BE: [Bb][Ee];
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
// ARRAY: '[' VALUE_LIST ']';
// fragment VALUE_LIST: (STRING | BOOLEAN | INTEGER) (',' (STRING | BOOLEAN | INTEGER))*;
STRING: '"' ('\\"'|.)*? '"';
BOOLEAN: (ENABLED | DISABLED | TRUE | FALSE);
INTEGER: [0-9]+;

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
PROPERTY_NAME: [a-zA-Z0-9_]+ ('.' [a-zA-Z0-9_]+)*;

WS: [ \t\r\n]+ -> skip;
SL_COMMENT: '//' .*? '\n' -> skip;