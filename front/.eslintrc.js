module.exports = {
    "env": "plugin:vue/essential",
    "extends": "eslint:recommended",
    "no-console": ["error", {allow: ["log", "warn", "error"]}],
    "parserOptions": {
        "ecmaVersion": 2016
    },

    "rules": {
        "indent": [
            "error",
            "tab"
        ],
        "linebreak-style": [
            "error",
            "unix"
        ],
        "quotes": [
            "error",
            "double"
        ],
        "semi": [
            "error",
            "never"
        ]
    },

    env: {
        browser: true,
        es6: true
    },

    parserOptions: {
        ecmaVersion: 2016
    },

    rules: {
        indent: [
            "error",
            "tab"
        ],
        "linebreak-style": [
            "error",
            "unix"
        ],
        quotes: [
            "error",
            "double"
        ],
        semi: [
            "error",
            "never"
        ]
    }
}