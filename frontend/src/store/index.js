import Vue from 'vue';
import Vuex from 'vuex';
import editorOptions from './editorOptions';

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        editorOptions : editorOptions
    },
    mutations: {
    },
    actions: {
    },
    modules: {
    },
    getters:{
        EDITOROPTIONS : state =>{
            return state.editorOptions;
        }
    }
});
