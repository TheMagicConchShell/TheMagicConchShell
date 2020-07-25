<template>
    <div>
        <div class="nav-bar-holder" />
        <b-navbar
            class="navbar sticky-top"
            toggleable="lg"
            type="dark"
        >
            <router-link :to="{name: 'Home'}">
                <b-navbar-brand>
                    Platform 이름
                </b-navbar-brand>
            </router-link>

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
                        disabled
                    >
                        Q&A
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
                    </b-nav-item-dropdown>
                </b-navbar-nav>
            </b-collapse>
        </b-navbar>
    </div>
</template>

<script>
export default {
    
};
</script>

<style scoped>
.nav-bar-holder {
    height: 30px;
}
.navbar {
    background-color: rgb(178, 87, 34);
    position: fixed;
    top: 0;
    width: 100%;
}

* {
    border: 1px solid black !important;
}
</style>
