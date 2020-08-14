import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

import i18n from '@/i18n';

export const state = {
    locale: 'ko',
};

export const mutations = {
    kor(state) {
        i18n.locale = 'ko';
        state.language='ko';
    },
    eng(state) {
        i18n.locale = 'en';
        state.language='en';
    },
    ch(state) {
        i18n.locale = 'ch';
        state.language='ch';
    },
    SET_LOCALE(state, locale) {
        i18n.locale = locale;
        state.locale = locale;
    },
};

export const getters = {
    currentlanguage: state => state.locale,
    language: state => state.locale,
};

export const actions = {
    setLocale({ commit }, locale) {
        commit('SET_LOCALE', locale);
    },
};

export const modules = {
};