import Vue from 'vue';
import Vuex from 'vuex';

import createPersistedState from "vuex-persistedstate";
import editorOptions from './editorOptions';

Vue.use(Vuex);

export default new Vuex.Store({
    plugins: [createPersistedState()],
    state: {
        nickname: "",
        jwtAuthToken: "",
        editorOptions : editorOptions,
    },
    mutations: {
        setJwtAuthToken(state, payload) {
            state.jwtAuthToken = payload;
        },
        setNickname(state, payload) {
            state.nickname = payload;
        },
    },
    getters: {
        jwtAuthToken(state) {
            return state.jwtAuthToken;
        },
        nickname(state) {
            return state.nickname;
        },
    },
    actions: {
        jwtAuthToken({ commit }, payload) {
            commit("setJwtAuthToken", payload);
        },
        nickname({ commit }, payload) {
            commit("setNickname", payload);
        },
    },
    getters:{
        EDITOROPTIONS : state =>{
            return state.editorOptions;
        }
    }
});
