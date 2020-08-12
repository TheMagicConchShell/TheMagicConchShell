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
    _$fetchCategories({ commit }) {
        return new Promise((resolve, reject) => {
            console.log('fetchStart');
            axios({
                url: '/category',
                method: 'get',
            })
                .then((response) => {
                    if (200 <= response.status && response.status < 300) {
                        commit('GET_CATEGORIES_SUCCESS', response.data.data);
                        commit('SET_AVAILABLE', true);

                        console.log('fetchEnd');
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
    createCategory({ dispatch }, { name, description }) {
        return new Promise((resolve, reject) => {
            axios({
                url: '/category',
                method: "post",
                data: {
                    name : name,
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
    updateCategory({ dispatch }, { source, destination, description }) {
        return new Promise((resolve, reject) => {
            axios({
                url: '/category',
                method: "put",
                data: {
                    curName : source,
                    changeName: destination,
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