import Vue from 'vue';
import Vuex from 'vuex';

import * as auth from '@/store/modules/auth';
import editorOptions from '@/store/editorOptions';

import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        auth,
    },
    plugins: [createPersistedState({
        storage: window.sessionStorage,
        paths: ['auth'],
    })],
    state: {
        editorOptions : editorOptions,
        language: 'ko'
    },
    getters:{
        EDITOROPTIONS : (state) => {
            return state.editorOptions;
        },
        currentlanguage: state => state.language
    },
    mutations: {
        kor(state) {
            state.language='en';
        },
        eng(state) {
            state.language='ko';
        }
    },
});
