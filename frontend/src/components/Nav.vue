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
                        src="../assets/images/Preview_Thumb_No_Alpha.jpg"
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
                    <b-nav-item href="#">
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
                            :to="{name: '???'}"
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
                        <b-dropdown-item href="#">
                            Profile
                        </b-dropdown-item>
                        <b-dropdown-item href="#">
                            Sign Out
                        </b-dropdown-item>
                        <b-dropdown-item v-b-modal.signin>
                            <Signin />
                        </b-dropdown-item>
                        <b-dropdown-item v-b-modal.login>
                            <Login />
                        </b-dropdown-item>
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
import Signin from '../views/Account/Signin.vue';
import Login from '../views/Account/Login.vue';

export default {
    name: 'Nav',
    components: {
        Signin,
        Login,
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
