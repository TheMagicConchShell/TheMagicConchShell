module.exports = {
    root: true,
    env: {
        node: true,
    },
    extends: [
        'plugin:vue/essential',
        'plugin:vue/strongly-recommended',
        'plugin:vue/recommended',
        '@vue/airbnb',
    ],
    parserOptions: {
        parser: 'babel-eslint',
        sourceType: 'module',
    },
    plugins: [
        'vue',
    ],
    rules: {
        'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
        'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
        indent: [
            'error',
            4,
        ],
        'vue/html-indent': ['error', 4, {
            attribute: 1,
            closeBracket: 0,
            alignAttributesVertically: true,
            ignores: [],
        }],
        'vue/html-closing-bracket-newline': ['error', {
            singleline: 'never',
            multiline: 'always',
        }],
        semi: [
            'error',
            'always',
        ],
        'linebreak-style': [
            'error',
            'windows',
        ],
    },
};
