lexer grammar CloudSpecLex;

// Date value
DATE_STRING: '"' DATE_FORMAT (' ' TIME_FORMAT)? '"';
STRING: '"' (~["\\\r\n] | '\\' (. | EOF))* '"';
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
USE_MODULE: USE ' ' MODULE;
USE_GROUP: USE ' ' GROUP;
USE_RULE: USE ' ' RULE;
END_PLAN: END ' ' PLAN;
END_MODULE: END ' ' MODULE;
END_GROUP: END ' ' GROUP;
END_RULE: END ' ' RULE;
PLAN: [Pp][Ll][Aa][Nn];
MODULE: [Mm][Oo][Dd][Uu][Ll][Ee];
GROUP: [Gg][Rr][Oo][Uu][Pp];
RULE: [Rr][Uu][Ll][Ee];
ON: [Oo][Nn];
WITH: [Ww][Ii][Tt][Hh];
ASSERT: [Aa][Ss][Ss][Ee][Rr][Tt];
AND: [Aa][Nn][Dd];
SET: [Ss][Ee][Tt];
INPUT: [Ii][Nn][Pp][Uu][Tt];
AS: [Aa][Ss];
fragment USE: [Uu][Ss][Ee];
fragment END: [Ee][Nn][Dd];

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

PROPERTY_REF: LETTER [a-zA-Z0-9_]* ':' PROPERTY_TYPE;
fragment PROPERTY_TYPE: (NUMBER_TYPE | STRING_TYPE | BOOLEAN_TYPE);
fragment NUMBER_TYPE: 'number';
fragment STRING_TYPE: 'string';
fragment BOOLEAN_TYPE: 'boolean';

// Resource and member references
RESOURCE_DEF_REF: PROVIDER_NAME ':' GROUP_NAME ':' RESOURCE_NAME;
CONFIG_REF: PROVIDER_NAME ':' CONFIG_NAME;
fragment PROVIDER_NAME: LETTER [a-zA-Z0-9_]*;
fragment GROUP_NAME: LETTER [a-zA-Z0-9_]*;
fragment RESOURCE_NAME: LETTER [a-zA-Z0-9_]*;
fragment CONFIG_NAME: LETTER [a-zA-Z0-9_]*;
MEMBER_NAME: LETTER [a-zA-Z0-9_]*;

WS: [ \t\r\n]+ -> skip;
SL_COMMENT: '#' .*? '\n' -> skip;