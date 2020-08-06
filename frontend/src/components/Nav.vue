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
                <b-navbar-nav class="ml-auto d-flex align-items-baseline">
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
                        <b-dropdown-item href="#">
                            EN
                        </b-dropdown-item>
                        <b-dropdown-item href="#">
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

export default {
    name: 'Nav',
    components: {
        Signup,
        Login,
    },
    data: () => ({
        language: '<i class="fas fa-language"></i>'
    }),
    computed: {
        nickname: {
            get() {
                return this.$store.getters.nickname;
            },
        }
    },
    methods: {
        logout() {
            this.$store.dispatch('logout');

            this.$toast('안내', '로그아웃 되었습니다.');
        },
        moveToUserDetail() {
            this.$router.push({
                'name': 'userdetail'
            });
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
    font-size: 130%;
}
#spot_area {
    position: fixed;
    top: 100px;
    width: 100%;
    z-index: 1;
}
.show {
    background-color: #BEDAE5!important;
    display: flex;
    padding: 10px;
}
.show.nav-item {
    color: black!important;
    z-index:2;
}
</style>
