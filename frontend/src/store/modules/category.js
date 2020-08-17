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
            const ko = e.name;
            e.name = {};
            e.name.ko = ko;
            e.name.en = e.enName || ko;
            e.name.ch = e.chName || ko;
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
    categoryNameById: (state) => (id, language = 'ko') => {
        return state.categoryMap[id].name[language];
    },
};

export const actions = {
    _$fetchCategories({ commit }) {
        return new Promise((resolve, reject) => {
            axios({
                url: '/category',
                method: 'get',
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
                });
        });
    },
    createCategory({ dispatch }, { name, enName, chName, description }) {
        return new Promise((resolve, reject) => {
            axios({
                url: '/category',
                method: "post",
                data: {
                    name : name,
                    enName : enName,
                    chName : chName,
                    description: description
                }
            }).then((response)=>{
                if (200 <= response.status && response.status < 300) {
                    dispatch('_$fetchCategories');
                    
                    resolve(response.data.data);
                } else {
                    reject(response);
                }
            }).catch((error)=>{
                reject(error);
            });
        });
    },
    updateCategory({ dispatch }, { source, destination, enName, chName, description }) {
        return new Promise((resolve, reject) => {
            axios({
                url: '/category',
                method: "put",
                data: {
                    curName : source,
                    changeName: destination,
                    enName : enName,
                    chName : chName,
                    description: description,
                }
            }).then((response)=>{
                if (200 <= response.status && response.status < 300) {
                    dispatch('_$fetchCategories');
                    
                    resolve(response.data.data);
                } else {
                    reject(response);
                }
            }).catch((error)=>{
                reject(error);
            });
        });
    },
    deleteCategory({ dispatch }, { categoryName }) {
        return new Promise((resolve, reject) => {
            axios({
                url: '/category',
                method: "delete",
                params:{
                    categoryName : categoryName,
                }
            }).then((response)=>{
                if (200 <= response.status && response.status < 300) {
                    dispatch('_$fetchCategories');

                    resolve(response.data.data);
                } else {
                    reject(response);
                }
            }).catch((error)=>{
                reject(error);
            });
        });
    },
    fetchCategories({ dispatch, state }) {
        if (state.available) {
            dispatch('_$fetchCategories');

            return new Promise((resolve) => {
                resolve(state.categories);
            });
        } else {
            return new Promise((resolve, reject) => {
                dispatch('_$fetchCategories')
                    .then((response) => {
                        resolve(response);
                    })
                    .catch((error) => {
                        reject(error);
                    });
            });
        }
    },
};

export const modules = {
};