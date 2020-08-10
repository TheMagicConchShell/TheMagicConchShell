<template>
    <div>
        <b-navbar
            class="navbar sticky-top"
            toggleable="lg"
            type="dark"
        >
            <b-navbar-brand class="p-0">
                <router-link
                    :to="{name: 'Home'}"
                    class="text-light text-decoration-none"
                >
                    <img
                        id="photo"
                        src="../assets/images/sora.png"
                    >
                    <transition name="conversion">
                        <span
                            v-if="language==='ko'"
                            key="1"
                        >
                            마법의 싸피고둥
                        </span>
                        <span
                            v-else
                            key="2"
                        >
                            Magic SSAFY conch
                        </span>
                    </transition>
                </router-link>
            </b-navbar-brand>

            <b-navbar-toggle target="nav-collapse" />

            <b-collapse
                id="nav-collapse"
                is-nav
            >
                <b-navbar-nav>
                    <b-nav-item>
                        <router-link
                            :to="{name: 'Support'}"
                            class="text-light text-decoration-none"
                        >
                            <transition name="conversion">
                                <span
                                    v-if="language==='ko'"
                                    key="1"
                                >
                                    공지사항
                                </span>
                                <span
                                    v-else
                                    key="2"
                                >
                                    Notice
                                </span>
                            </transition>
                        </router-link>
                    </b-nav-item>
                    <b-nav-item>
                        <router-link
                            :to="{name: ''}"
                            class="text-light text-decoration-none"
                        >
                            <transition name="conversion">
                                <span
                                    v-if="language==='ko'"
                                    key="1"
                                >
                                    문의
                                </span>
                                <span
                                    v-else
                                    key="2"
                                >
                                    Q&A
                                </span>
                            </transition>
                        </router-link>
                    </b-nav-item>
                    <b-nav-item>
                        <router-link
                            :to="{name: 'List'}"
                        >
                            <transition name="conversion">
                                <span
                                    v-if="language==='ko'"
                                    key="1"
                                >
                                    고민게시판
                                </span>
                                <span
                                    v-else
                                    key="2"
                                >
                                    Counsel
                                </span>
                            </transition>
                        </router-link>
                    </b-nav-item>
                    <b-nav-item>
                        <router-link
                            :to="{name: 'Rank'}"
                            class="text-light text-decoration-none"
                        >
                            <transition name="conversion">
                                <span
                                    v-if="language==='ko'"
                                    key="1"
                                >
                                    랭킹
                                </span>
                                <span
                                    v-else
                                    key="2"
                                >
                                    Ranking
                                </span>
                            </transition>
                        </router-link>
                    </b-nav-item>
                </b-navbar-nav>

                <!-- Right aligned nav items -->
                <b-navbar-nav class="ml-auto d-flexalign-items-baseline">
                    <div id="rightnav">
                        <b-nav-form>
                            <b-form-input
                                size="sm"
                                class="mr-sm-2"
                                placeholder="Search"
                            />
                            <b-button
                                id="commonbutton"
                                size="sm"
                                class="my-2 mr-2 my-sm-0"
                                type="submit"
                            >
                                Search
                            </b-button>
                        </b-nav-form>

                        <b-nav-item-dropdown right>
                            <template v-slot:button-content>
                                <i
                                    class="fas fa-language"
                                    style="color: #6B799E;"
                                />
                            </template>
                            <b-dropdown-item @click="setkor">
                                EN
                            </b-dropdown-item>
                            <b-dropdown-item @click="seteng">
                                한국어
                            </b-dropdown-item>
                        </b-nav-item-dropdown>

                        <b-nav-item-dropdown right>
                            <!-- Using 'button-content' slot -->
                            <template v-slot:button-content>
                                <i
                                    class="fas fa-users"
                                    style="color: #6B799E;"
                                />
                            </template>
                            <div v-if="nickname">
                                <b-dropdown-item 
                                    @click.prevent="moveToUserDetail"
                                >
                                    Profile
                                </b-dropdown-item>
                                <b-dropdown-item @click.prevent="logout">
                                    Log out
                                </b-dropdown-item>
                            </div>

                            <div v-else>
                                <b-dropdown-item v-b-modal.signup>
                                    <Signup />
                                </b-dropdown-item>
                                <b-dropdown-item v-b-modal.login>
                                    <Login />
                                </b-dropdown-item>
                            </div>
                        </b-nav-item-dropdown>
                    </div>
                </b-navbar-nav>
            </b-collapse>
            <!-- spot area -->
        </b-navbar>
        <div
            id="spot_area"
            class="sticky-top"
        >
            여기는 spot area 영역
        </div>
    </div>
</template>

<script>
import {mapState, mapActions} from 'vuex';
import Signup from '@/components/account/Signup.vue';
import Login from '@/components/account/Login.vue';

export default {
    name: 'Nav',
    components: {
        Signup,
        Login,
    },
    computed: {
        ...mapState(['language']),
        ...mapState({
            nickname: state => state.auth.nickname,
        })
    },
    methods: {
        ...mapActions(['setkor', 'seteng']),
        logout() {
            this.$store.dispatch('logout');

            this.$toast('안내', '로그아웃 되었습니다.');
        },
        moveToUserDetail() {
            this.$router.push({
                'name': 'userdetail'
            });
        },
        setkor() {
            this.$store.commit('kor');
        },
        seteng() {
            this.$store.commit('eng');            
        }
    },
};
</script>

<style scoped>
.navbar {
    background-color:  #A6C2CE;
    opacity: 0.8;
    position: fixed;
    top: 0;
    width: 100%;
    height: 68px;
    margin: 0;
    padding: 0;
    font-size: 130%;
}
#photo {
    height: 64px; 
    width: auto;
}
@media (max-width: 992px) {
    .navbar {
        height: 52px;
        font-size: 100%;
    }
    #spot_area {
        position: fixed;
        top: 52px!important;
        font-size: 75%;  
    }
    #photo {
        height: 48px;
    }
}
#spot_area {
    position: fixed;
    top: 68px;
    width: 100%;
    z-index: 1;
}
.show {
    background-color: #BEDAE5!important;
    display: flex;
    padding: 10px;
}
a {
    color: white;
}
.show.nav-item {
    color: black!important;
    z-index:2;
}
#rightnav {
    display: flex;
    justify-content: end;
}
.nav-item.b-nav-dropdown.dropdown {
    height:48px;
}
.dropdown-menu.dropdown-menu-right.show {
    position: fixed;
    z-index:5;
}
</style>
