import Vue from 'vue';
import Vuex from 'vuex';
import axios from '@/axios';

Vue.use(Vuex);

export const state = {
    categories: [],
    available: false,
    categoryMap: {},
};

export const mutations = {
    GET_CATEGORIES_SUCCESS(state, categories) {
        state.categories = categories;
        categories.forEach((e) => {
            state.categoryMap[e.id] = e;
        });
    },
    SET_AVAILABLE(state, status) {
        state.available = status;
    },
};

export const getters = {
    categories: (state) => {
        return state.categories;
    },
    categoryNameById: (state) => (id) => {
        return state.categoryMap[id].name;
    },
};

export const actions = {
    fetchCategories({state, commit}) {
        if (state.available) {
            return new Promise((resolve) => {
                resolve(state.categories);
            });
        } else {
            return new Promise((resolve, reject) => {
                axios({
                    method: 'get',
                    url: '/counsel/category',
                })
                    .then((response) => {
                        if (200 <= response.status && response.status < 300) {
                            commit('GET_CATEGORIES_SUCCESS', response.data.data);
                            commit('SET_AVAILABLE', true);
                            resolve(response.data.data);
                        } else {
                            commit('SET_AVAILABLE', false);
                            reject(response);
                        }
                    })
                    .catch((error) => {
                        commit('SET_AVAILABLE', false);
                        reject(error);
                    }).finally(() => {
                    });
            });
        }
    },
};

export const modules = {
};