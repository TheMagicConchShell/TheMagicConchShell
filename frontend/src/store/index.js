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
        paths: ['auth'],
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
