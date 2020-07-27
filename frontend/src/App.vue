<template>
    <div id="app">
        <Nav v-show="nowPlatform" />
        <div id="content">
            <router-view />
            <div
                v-show="isnotTop"
                id="go-up"
            >
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
import Nav from './components/Nav.vue';

export default {
    name: 'App',
    components: {
        Nav,
    },
    data() {
        return {
            nowPlatform: true,
            isnotTop: false,
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
            this.nowPlatform = nowPlatform;
        },
        top() {
            while (document.documentElement.scrollTop !== 0) {
                setTimeout(this.scrolling(), 200);
            }
        },
        scrolling() {
            const y = document.documentElement.scrollTop;
            const step = 2;
            // this.y -= step;
            window.scrollTo(0, y - step);
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
  background: linear-gradient( 0, rgb(178, 87, 34), rgb(190, 158, 108));
  width: 50px;
  height: 50px;
  border: 1px solid #ffffff;
  border-radius: 5px;
}

#go-up :hover {
  cursor: pointer;
}
</style>
