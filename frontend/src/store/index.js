import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        language: 'ko'
    },
    getters: {
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
    actions: {
    },
    modules: {
    },
});
