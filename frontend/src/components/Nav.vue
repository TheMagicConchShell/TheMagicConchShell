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
                    마법의 싸피고둥
                </router-link>
            </b-navbar-brand>

            <b-navbar-toggle target="nav-collapse" />

            <b-collapse
                id="nav-collapse"
                is-nav
            >
                <b-navbar-nav>
                    <b-nav-item
                        href="/support"
                    >
                        공지사항
                    </b-nav-item>
                    <b-nav-item
                        href="#"
                    >
                        Q&A
                    </b-nav-item>
                    <b-nav-item>
                        <router-link
                            :to="{name: 'List'}"
                            class="text-light text-decoration-none"
                        >
                            고민게시판
                        </router-link>
                    </b-nav-item>
                    <b-nav-item>
                        <router-link
                            :to="{name: 'Rank'}"
                            class="text-light text-decoration-none"
                        >
                            랭킹
                        </router-link>
                    </b-nav-item>
                </b-navbar-nav>

                <!-- Right aligned nav items -->
                <b-navbar-nav class="ml-auto">
                    <b-nav-form>
                        <b-form-input
                            size="sm"
                            class="mr-sm-2"
                            placeholder="Search"
                        />
                        <b-button
                            id="commonbutton"
                            size="sm"
                            class="my-2 my-sm-0"
                            type="submit"
                        >
                            Search
                        </b-button>
                    </b-nav-form>

                    <b-nav-item-dropdown
                        right
                        text="language"
                    >
                        <b-dropdown-item href="#">
                            EN
                        </b-dropdown-item>
                        <b-dropdown-item href="#">
                            ES
                        </b-dropdown-item>
                        <b-dropdown-item href="#">
                            RU
                        </b-dropdown-item>
                        <b-dropdown-item href="#">
                            FA
                        </b-dropdown-item>
                    </b-nav-item-dropdown>

                    <b-nav-item-dropdown right>
                        <!-- Using 'button-content' slot -->
                        <template v-slot:button-content>
                            <em>User</em>
                        </template>
                        <div v-if="isLogin">
                            <b-dropdown-item v-b-modal.userdetail>
                                <UserDetail />
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
import Signup from '@/components/account/Signup.vue';
import Login from '@/components/account/Login.vue';
import UserDetail from '@/components/account/UserDetail.vue';

const storage = window.sessionStorage;

export default {
    name: 'Nav',
    components: {
        Signup,
        Login,
        UserDetail,
    },
    data: () => ({
        isLogin: '',
    }),
    created() {
        this.init();
    },
    methods: {
        init() {
            if(storage.getItem('jwt-auth-token')){
                this.isLogin = true;
            } else {
                this.isLogin = false;
            }
        },
        logout() {
            storage.setItem('jwt-auth-token', '');
            storage.setItem('login_user', '');
            this.isLogin = false;
            this.$toast('안내', '로그아웃 되었습니다.');
        },
    },
};
</script>

<style scoped>
.navbar {
    background-color:  #A6C2CE;
    position: fixed;
    top: 0;
    width: 100%;
    height: 100px;
    margin: 0;
    padding: 0;
}
#spot_area {
    position: fixed;
    top: 100px;
    width: 100%;
}
</style>
