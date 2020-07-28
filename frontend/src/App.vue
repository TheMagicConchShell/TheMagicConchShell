<template>
    <div id="app">
        <PlatformNav v-show="nowPlatform" />
        <div id="content">
            <router-view />
            <div id="go-up">
                <button
                    id="go-up"
                >
                    <i
                        class="fas fa-angle-double-up"
                        @click="top"
                    />
                </button>
            </div>
        </div>
    </div>
</template>

<script>
import PlatformNav from './components/PlatformNav.vue';

export default {
    name: 'App',
    components: {
        PlatformNav,
    },
    data() {
        return {
            nowPlatform: true,
        };
    },
    watch: {
        $route(to) {
            this.checkUrl(to.name);
        },
    },
    created() {
        const url = this.$route.name;

        this.checkUrl(url);
    },
    methods: {
        checkUrl(url) {
            const array = [
                'Blog',
            ];

            let nowPlatform = true;
            array.some((path) => {
                if (url === path) { nowPlatform = false; return nowPlatform; }
                return nowPlatform;
            });
            console.log(nowPlatform);
            this.nowPlatform = nowPlatform;
        },
        top() {
            window.scrollTo(0, 0);
        },
    },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}

#go-up {
  margin: 0;
  padding: 0;
  position:fixed;
  right: 20px;
  bottom: 20px;
  color: #ffffff;
  background-color: rgb(178, 87, 34);
  width: 50px;
  height: 50px;
  border: 1px solid #ffffff;
  border-radius: 5px;
}

#go-up :hover {
  background-color: firebrick;
  cursor: pointer;
}

#content {
    margin-top: 94px;
}
</style>
