<template>
    <!-- 모바일용 바텀바 -->
    <div>
        <input
            id="inp"
            type="checkbox"
            @click="clicked =!clicked"
        >
        <label for="inp">
            <div id="bottombar_button">
                <i 
                    id="clockwise"
                    class="fas fa-bars"
                    style="color: #bdbdbd;font-size: 150%;"
                />
            </div>
        </label>
        <div 
            id="bottombar"
        >
            <ul>
                <li>
                    <router-link
                        :to="{name: 'Support'}"
                        class="text-light text-decoration-none d-flex justify-content-between"
                    >
                        <i class="fas fa-bullhorn" />
                        <div id="bottombar_menu">
                            <transition
                                name="conversion"
                            >
                                <span :key="language">{{ $t('title.notice') }}</span>
                            </transition>
                        </div>
                    </router-link>
                </li>
                <li>
                    <router-link
                        :to="{name: 'QnaBoard'}"
                        class="text-light text-decoration-none d-flex justify-content-between"
                    >
                        <i class="fas fa-question-circle" />
                        <div id="bottombar_menu">
                            <transition name="conversion">
                                <span :key="language">{{ $t('title.qna') }}</span>
                            </transition>
                        </div>
                    </router-link>
                </li>
                <li>
                    <router-link
                        :to="{name: 'List'}"
                        class="text-light text-decoration-none d-flex justify-content-between"
                    >
                        <div>
                            <i class="far fa-meh-rolling-eyes" /> <i class="fas fa-comment-dots" />
                        </div>
                        <div id="bottombar_menu">
                            <transition name="conversion">
                                <span :key="language">{{ $t('title.counselBoard') }}</span>
                            </transition>
                        </div>
                    </router-link>
                </li>
                <li>
                    <router-link
                        :to="{name: 'Rank'}"
                        class="text-light text-decoration-none d-flex justify-content-between"
                    >
                        <i class="fas fa-crown" />
                        <div id="bottombar_menu">
                            <transition name="conversion">
                                <span :key="language">{{ $t('title.ranking') }}</span>
                            </transition>
                        </div>
                    </router-link>
                </li>
                <li>
                    <div
                        class="sidebar-item text-decoration-none d-flex justify-content-between"
                        @click="openChat"
                    >
                        <i class="far fa-comments" />
                        <div
                            id="bottombar_menu"
                        >
                            <transition name="conversion">
                                <span :key="language">{{ $t('title.chatting') }}</span>
                            </transition>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
        <!-- 웹용 사이드바 -->
        <div
            id="sidebar"
            @mouseover="mouseon"
            @mouseleave="mouseoff"
        >
            <ul>
                <li>
                    <router-link
                        :to="{name: 'Support'}"
                        class="text-light text-decoration-none d-flex"
                    >
                        <i class="fas fa-bullhorn" />
                        <div
                            v-if="mouseover"
                            id="sidebar-menu"
                        >
                            <transition
                                name="conversion"
                            >
                                <span :key="language">{{ $t('title.notice') }}</span>
                            </transition>
                        </div>
                    </router-link>
                </li>
                <li>
                    <router-link
                        :to="{name: 'QnaBoard'}"
                        class="text-light text-decoration-none d-flex"
                    >
                        <i class="fas fa-question-circle" />
                        <div
                            v-if="mouseover"
                            id="sidebar-menu"
                        >
                            <transition name="conversion">
                                <span :key="language">{{ $t('title.qna') }}</span>
                            </transition>
                        </div>
                    </router-link>
                </li>
                <li>
                    <router-link
                        :to="{name: 'List'}"
                        class="text-light text-decoration-none d-flex"
                    >
                        <i class="far fa-meh-rolling-eyes mr-1" /> <i class="fas fa-comment-dots" />
                        <div
                            v-if="mouseover"
                            id="sidebar-menu"
                        >
                            <transition name="conversion">
                                <span :key="language">{{ $t('title.counselBoard') }}</span>
                            </transition>
                        </div>
                    </router-link>
                </li>
                <li>
                    <router-link
                        :to="{name: 'Rank'}"
                        class="text-light text-decoration-none d-flex"
                    >
                        <i class="fas fa-crown" />
                        <div
                            v-if="mouseover"
                            id="sidebar-menu"
                        >
                            <transition name="conversion">
                                <span :key="language">{{ $t('title.ranking') }}</span>
                            </transition>
                        </div>
                    </router-link>
                </li>
                <li>
                    <div
                        class="sidebar-item text-decoration-none d-flex"
                        @click="openChat"
                    >
                        <i class="far fa-comments" />
                        <div
                            v-if="mouseover"
                            id="sidebar-menu"
                        >
                            <transition name="conversion">
                                <span :key="language">{{ $t('title.chatting') }}</span>
                            </transition>
                        </div>
                    </div>
                </li>
            </ul>
        </div>

        <nav class="navbar navbar-light bg-light sticky-top">
            <div id="logo">
                <router-link
                    :to="{name: 'Home'}"
                    class="text-dark text-decoration-none"
                >
                    <transition name="conversion">
                        <span :key="language">
                            <img
                                id="photo"
                                src="../assets/images/sora.png"
                            >
                            {{ $t('title.title') }}
                        </span>
                    </transition>
                </router-link>
            </div>
            <ul 
                id="navitems"
                class="navbar-nav"
            >
                <li 
                    id="ddmenu"
                    class="nav-item dropdown"
                >
                    <div
                        class="nav-link"
                    >
                        <i
                            class="fas fa-language"
                        />
                        <ul id="ddopen">
                            <li
                                v-for="language in languages"
                                :key="language.name"
                                @click.prevent="changeLanguage(language.value)"
                            >
                                {{ language.name }}
                            </li>
                        </ul>
                    </div>
                </li>
                <li 
                    id="ddmenu"
                    class="nav-item dropdown"
                >
                    <div
                        class="nav-link"
                    >
                        <i
                            class="fas fa-users"
                        />
                        <ul id="ddopen">
                            <div v-if="nickname">
                                <li @click.prevent="moveToUserDetail">
                                    {{ $t('layout.profile') }}
                                </li>
                                <li @click.prevent="logout">
                                    {{ $t('layout.logout') }}
                                </li>
                                <div v-if="nickname=='admin'">
                                    <li @click.prevent="moveManagerPage">
                                        {{ $t('layout.manage') }}
                                    </li>
                                </div>
                            </div>
                            <div v-else>
                                <li v-b-modal.signup>
                                    <Signup />
                                </li>
                                <li v-b-modal.login>
                                    <Login />
                                </li>
                            </div>
                        </ul>
                    </div>
                </li>
            </ul>
        </nav>
        <div
            id="spot_area"
            class="sticky-top"
        >
            <!-- 여기는 spot area 영역 -->
            <carousel 
                id="spotCarousel"
                :per-page="1"
                :autoplay="true"
                :autoplay-direction="'forward'"
                :autoplay-timeout="10000"
                :loop="true"
                :pagination-enabled="false"
                :speed="8000"
            >
                <template v-if="spotList && spotList.length">
                    <slide
                        v-for="item in spotList"
                        :key="item.no"
                    >
                        <router-link
                            :to="{name: 'CounselDetail', params: {no: item.no}}"
                            style="text-decoration: none;"
                        >
                            <span> [ 광고 ] {{ item.title }}</span>
                        </router-link>
                    </slide>
                </template>   
            </carousel>
        </div>
        <div 
            v-if="chatStatus" 
        >
            <ChatWindow 
                ref="chatwindow"
                :close-handler="closeChat"
            />
        </div>
    </div>
</template>

<script>
import {mapGetters, mapState, mapActions} from 'vuex';
import Signup from '@/components/account/Signup.vue';
import Login from '@/components/account/Login.vue';
import ChatWindow from '@/components/chat/ChatWindow.vue';
import { Carousel, Slide } from 'vue-carousel';

export default {
    name: 'Nav',
    components: {
        Signup,
        Login,
        Carousel,
        Slide,
        ChatWindow
    },
    data() {
        return {
            languages: [
                {
                    name: '한국어',
                    value: 'ko',
                },
                {
                    name: 'English',
                    value: 'en',
                },
                {
                    name: '中文',
                    value: 'ch',
                }
            ],
            mouseover: false,
            clicked: false,
            spotList: [],
            autoplay: "true",
            chatStatus:false
        };
    },
    computed: {
        ...mapGetters(['language']),
        ...mapGetters(['nickname']),
    },
    watch: {
        $route () { 
            const element = document.getElementById('inp');
            element.checked = false;
            this.clicked = false;
            this.showCraousel();
            this.fetchSpotList();
        }
    },
    created() {
        this.fetchSpotList();
    },
    methods: {
        ...mapActions(['setkor', 'seteng']),
        logout() {
            if (this.$refs.chatwindow) {
                this.$refs.chatwindow.leaved();
            }
            this.chatStatus = false;
            this.$store.dispatch('logout');
            if(Kakao.Auth.getAccessToken()!=null){
                Kakao.Auth.logout(function(){
                    
                });
            }
            
            this.$toast('안내', '로그아웃 되었습니다.');
            this.$router.push({ name: 'Home'}).catch(() => {});
        },
        moveToUserDetail() {
            this.$router.push({
                'name': 'userdetail'
            })
                .catch(() => {});
        },
        changeLanguage(locale) {
            this.$store.dispatch('setLocale', locale);
        },
        moveManagerPage() {
            this.$router.push({
                'name': 'managepage'
            });
        },
        mouseon() {
            this.mouseover = true;
        },
        mouseoff() {
            this.mouseover = false;
        },
        clicking() {
            this.clicked = true;
        }, 
        fetchSpotList(){
            this.$axios({
                method: 'get',
                url: '/spot/banner',
            })
                .then((res)=>{
                    this.spotList = res.data.data;
                })
                .catch((error)=>{
                });
        },
        showCraousel(){
            const showme = document.getElementById('spotCarousel');
        },
        openChat(){
            if(this.nickname){
                this.chatStatus = true;
            }else{
                this.$toast("채팅","채팅은 로그인 후 이용 가능한 서비스입니다");
            }
        },
        closeChat(){
            this.chatStatus = false;
        }
    },
};

</script>

<style scoped>
.sidebar-item {
    color: #f8f9fa !important;
}
.sidebar-item:hover {
    cursor: pointer;
    color: #cbd3da !important;
}
a{
    color:unset;
}
a:hover {
    text-decoration: unset;
    color: #0363BA;
}

#inp {
    display:none;
}
#bottombar_button {
    z-index:2;
    cursor: pointer;
    visibility: hidden;
    opacity: 0;
    position: fixed;
    top: 0;
    left: 16px;
    height: 36px;
    width: 36px;
    margin: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid #bdbdbd;
    border-radius: 5px;
}
#bottombar {
    opacity: 0;
    visibility: hidden;
    position: fixed;
    top: 70px;
    /* height: 0; */
    width: 0;
    transition-duration: 0.5s;
    z-index :2;
}
#sidebar {
    font-family: sb;
    z-index: 2;
    display:flex;
    flex-direction: column;
    position:fixed;
    width: 60px;
    top: 0;
    background-color: #0363BA;
    height: 100vh;
    transition-duration: 0.5s;
}
#sidebar ul {
    padding-left: 16px;
}
#sidebar ul li {
    margin: 32px 0;
}
#sidebar-menu {
    position: absolute;
    right :16px;
}
#sidebar:hover {
    width: 200px;
    transition-duration: 0.5s;
}
.navbar {
    z-index:1;
    background-color:  white;
    opacity: 0.8;
    position: fixed;
    top: 0;
    width: 100%;
    height: 68px;
    margin: 0;
    padding: 0;
    font-size: 130%;
}
#logo {
    width: 100%;
    justify-self: center;
    font-family: bd;
}
#navitems {
    position:fixed;
    right: 0;
    margin-right: 16px;
    display:flex;
    flex-direction: row;
    z-index: 2;
}
#photo {
    height: 64px; 
    width: auto;
}
#ddmenu {
    padding: 0 16px 0 16px;
    position:relative;
}
#navitems #ddmenu:hover #ddopen {
    display:block;
    z-index:2;
    height: auto;
}
#ddopen {
    position: absolute;
    margin: 8px 0 0 0;
    padding: 4px;
    justify-content: end;
    display:none;
    width: 80px;
    left: -12px;
    font-size: 75%;
    border: 1px solid #bdbdbd;
    background-color: #f1f1f1;
    z-index:2;
}
#ddopen li {
    transition-duration: 0.5s;
}
#ddopen li:hover {
    background-color: #bdbdbd;
}
#spot_area {
    position: fixed;
    top: 68px;
    width: 100%;
    z-index:3;
}
.show {
    background-color: #BEDAE5!important;
    display: flex;
    padding: 10px;
}
.nav-link:hover {
    cursor: pointer;
}
@media (max-width: 992px) {
    #ddopen {
        font-size: 85%;
    }
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
    #sidebar {
        opacity: 0;
        visibility: hidden;
    }
    #bottombar ul {
        display: flex;
        flex-direction: column;
        padding: 16px;
        margin: 0;
    }
    #bottombar_button {
        visibility: visible;
        opacity: 1;
    }
    #inp:checked ~ #bottombar {
        opacity: 1!important;
        visibility: visible!important;
        background-color: #bdbdbd;
        /* height: 128px; */
        width: 150px;
    }
}
@media (max-width: 600px) {
    #ddopen {
        font-size: 100%;
    }
}
</style>
