import Vue from 'vue';
import Vuex from 'vuex';

import * as auth from '@/store/modules/auth';
import * as category from '@/store/modules/category';
import editorOptions from '@/store/editorOptions';

import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        auth,
        category,
    },
    plugins: [createPersistedState({
        storage: window.sessionStorage,
        paths: [
            'auth',
            'category',
        ],
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
            state.language='ko';
        },
        eng(state) {
            state.language='en';
        },
        ch(state) {
            state.language='ch';
        }
    },
});
