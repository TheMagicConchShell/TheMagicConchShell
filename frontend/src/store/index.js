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
        editorOptions : editorOptions
    },
    getters:{
        EDITOROPTIONS : (state) => {
            return state.editorOptions;
        }
    },
});
