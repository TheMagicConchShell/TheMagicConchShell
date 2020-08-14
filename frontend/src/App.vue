<template>
    <div id="app">
        <Nav
            v-show="nowPlatform"
            @language="languageset"
        />
        <div id="content">
            <router-view />
            <transition name="bottom-side">
                <button
                    v-show="windowTop"
                    id="go-up"
                >
                    <i
                        class="fas fa-angle-double-up"
                        @click="top"
                    />
                </button>
            </transition>
        </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import Nav from './components/Nav.vue';

export default {
    name: 'App',
    components: {
        Nav,
    },
    data() {
        return {
            nowPlatform: true,
            windowTop: window.top.scrollY,
        };
    },
    computed: {
        ...mapGetters(['language']),
    },
    watch: {
        $route(to) {
            this.checkUrl(to.name);
        },
        language: {
            immediate: true,
            handler() {
                document.title = this.$t('title.title');
            },
        },
    },
    created() {
        let locale = navigator.language || navigator.userLanguage;
        locale = locale.substring(0, 2);
        if (locale !== 'ko') locale = 'en';
        this.$i18n.locale = locale;

        const url = this.$route.name;

        this.checkUrl(url);
    },
    mounted() {
        window.addEventListener("scroll", this.onScroll);
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
        onScroll(e) {
            this.windowTop = window.top.scrollY;
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
        languageset(language) {
            this.language = language;
        }
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
  z-index: 1;

}
#app::before {
  content: '';
  display: block;
  position: absolute;
  width: 100%;
  height: 100%;
  background-image: url('assets/images/pattern.png');
  opacity: 0.08;
  background-size:contain;
  z-index: -1;
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
  background: linear-gradient( 0, #9C8F96, #ccbbc4);
  width: 30px;
  height: 30px;
  border: 1px solid #ffffff;
  border-radius: 5px;
}

#go-up :hover {
  cursor: pointer;
}
@media (max-width: 992px) {
    #content {
        margin: 70px 0px!important;
        padding: 0 10px;
    }
}   
</style>
