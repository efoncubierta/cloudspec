grammar CloudSpec;

spec: specDecl groupDecl+;

specDecl: SPEC STRING;

groupDecl: GROUP STRING ruleDecl+;

ruleDecl: RULE STRING onDecl withDecl? assertDecl;

onDecl: ON RESOURCE_DEF_REF;

withDecl: WITH statement andDecl*;

assertDecl: ASSERT statement andDecl*;

andDecl: AND statement;

predicate: IS? ('==' | EQUAL TO) value           # PropertyEqualPredicate
         | IS? ('!=' | NOT EQUAL TO) value       # PropertyNotEqualPredicate
         | IS? WITHIN array                      # PropertyWithinPredicate
         | IS? NOT WITHIN array                  # PropertyNotWithinPredicate
         | IS? ('<' | LESS_THAN) value           # PropertyLessThanPredicate
         | IS? ('<=' | LESS_THAN_EQUAL) value    # PropertyLessThanEqualPredicate
         | IS? ('>' | GREATER_THAN) value        # PropertyGreaterThanPredicate
         | IS? ('>=' | GREATER_THAN_EQUAL) value # PropertyGreaterThanEqualPredicate
         | IS? BETWEEN value AND value           # PropertyBetweenPredicate
         | STARTING_WITH value                   # PropertyStartingWithPredicate
         | ENDING_WITH value                     # PropertyEndingWithPredicate
         | CONTAINING value                      # PropertyContainingPredicate
         | IS? ENABLED                           # PropertyEnabledPredicate
         | IS? DISABLED                          # PropertyDisabledPredicate
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
LESS_THAN: (LESS THAN | LT);
GREATER_THAN: (GREATER THAN | GT);
LESS_THAN_EQUAL: (LESS THAN OR EQUAL TO | LTE);
GREATER_THAN_EQUAL: (GREATER THAN OR EQUAL TO | LTE);
STARTING_WITH: [Ss][Tt][Aa][Rr][Tt][Ii][Nn][Gg] WITH;
ENDING_WITH: [Ee][Nn][Dd][Ii][Nn][Gg] WITH;
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
EQUAL: [Ee][Qq][Uu][Aa][Ll];
ENABLED: [Ee][Nn][Aa][Bb][Ll][Ee][Dd];
DISABLED: [Dd][Ii][Ss][Aa][Bb][Ll][Ee][Dd];
TRUE: [Tt][Rr][Uu][Ee];
FALSE: [Ff][Aa][Ll][Ss][Ee];
HAS: [Hh][Aa][Ss];
BEFORE: [Bb][Ee][Ff][Oo][Rr][Ee];
AFTER: [Aa][Ff][Tt][Ee][Rr];
NOT: [Nn][Oo][Tt];
TO: [Tt][Oo];
GT: [Gg][Tt];
GTE: [Gg][Tt][Ee];
LT: [Ll][Tt];
LTE: [Ll][Tt][Ee];
LESS: [Ll][Ee][Ss][Ss];
GREATER: [Gg][Rr][Ee][Aa][Tt][Ee][Rr];
THAN: [Tt][Hh][Aa][Nn];
BETWEEN: [Bb][Ee][Tt][Ww][Ee][Ee][Nn];

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