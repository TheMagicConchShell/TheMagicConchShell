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
                    class="fas fa-grip-lines"
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
                                <span
                                    v-if="language==='ko'"
                                    key="1"
                                >
                                    공지사항
                                </span>
                                <span
                                    v-else-if="language==='en'"
                                    key="2"
                                >
                                    Notice
                                </span>
                                <span
                                    v-else
                                    key="3"
                                >
                                    注意
                                </span>
                            </transition>
                        </div>
                    </router-link>
                </li>
                <li>
                    <router-link
                        :to="{name: ''}"
                        class="text-light text-decoration-none d-flex justify-content-between"
                    >
                        <i class="fas fa-question-circle" />
                        <div id="bottombar_menu">
                            <transition name="conversion">
                                <span
                                    v-if="language==='ko'"
                                    key="1"
                                >
                                    문의
                                </span>
                                <span
                                    v-else-if="language==='en'"
                                    key="2"
                                >
                                    Q&A
                                </span>
                                <span
                                    v-else
                                    key="3"
                                >
                                    問題
                                </span>
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
                                <span
                                    v-if="language==='ko'"
                                    key="1"
                                >
                                    고민게시판
                                </span>
                                <span
                                    v-else-if="language==='en'"
                                    key="2"
                                >
                                    Counsel
                                </span>
                                <span
                                    v-else
                                    key="3"
                                >
                                    擔心 公告欄
                                </span>
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
                                <span
                                    v-if="language==='ko'"
                                    key="1"
                                >
                                    랭킹
                                </span>
                                <span
                                    v-else-if="language==='en'"
                                    key="2"
                                >
                                    Ranking
                                </span>
                                <span
                                    v-else
                                    key="3"
                                >
                                    排行
                                </span>
                            </transition>
                        </div>
                    </router-link>
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
                                <span
                                    v-if="language==='ko'"
                                    key="1"
                                >
                                    공지사항
                                </span>
                                <span
                                    v-else-if="language==='en'"
                                    key="2"
                                >
                                    Notice
                                </span>
                                <span
                                    v-else
                                    key="3"
                                >
                                    注意
                                </span>
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
                                <span
                                    v-if="language==='ko'"
                                    key="1"
                                >
                                    문의
                                </span>
                                <span
                                    v-else-if="language==='en'"
                                    key="2"
                                >
                                    Q&A
                                </span>
                                <span
                                    v-else
                                    key="3"
                                >
                                    問題
                                </span>
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
                                <span
                                    v-if="language==='ko'"
                                    key="1"
                                >
                                    고민게시판
                                </span>
                                <span
                                    v-else-if="language==='en'"
                                    key="2"
                                >
                                    Counsel
                                </span>
                                <span
                                    v-else
                                    key="3"
                                >
                                    擔心 公告欄
                                </span>
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
                                <span
                                    v-if="language==='ko'"
                                    key="1"
                                >
                                    랭킹
                                </span>
                                <span
                                    v-else-if="language==='en'"
                                    key="2"
                                >
                                    Ranking
                                </span>
                                <span
                                    v-else
                                    key="3"
                                >
                                    排行
                                </span>
                            </transition>
                        </div>
                    </router-link>
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
                        <span
                            v-if="language==='ko'"
                            key="1"
                        >
                            <img
                                id="photo"
                                src="../assets/images/sora.png"
                            >
                            마법의 싸피고둥
                        </span>
                        <span
                            v-else-if="language==='en'"
                            key="2"
                        >
                            <img
                                id="photo"
                                src="../assets/images/sora.png"
                            >
                            Magic SSAFY conch
                        </span>
                        <span
                            v-else
                            key="3"
                        >   
                            <img
                                id="photo"
                                src="../assets/images/sora.png"
                            >               
                            神奇的 SSAFY 海螺
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
                    <a
                        class="nav-link"
                        href="#"
                    >
                        <i
                            class="fas fa-language"
                        />
                        <ul id="ddopen">
                            <li @click.prevent="setkor">
                                한국어
                            </li>
                            <li @click.prevent="seteng">
                                English
                            </li>
                            <li @click.prevent="setch">
                                中文
                            </li>
                        </ul>
                    </a>
                </li>
                <li 
                    id="ddmenu"
                    class="nav-item dropdown"
                >
                    <a
                        class="nav-link"
                        href="#"
                    >
                        <i
                            class="fas fa-users"
                        />
                        <ul id="ddopen">
                            <div v-if="nickname">
                                <li @click.prevent="moveToUserDetail">
                                    Profile
                                </li>
                                <li @click.prevent="logout">
                                    Logout
                                </li>
                                <div v-if="nickname=='admin'">
                                    <li @click.prevent="moveManagerPage">
                                        Manage Page
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
                    </a>
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
                :autoplay-timeout="3000"
                :loop="true"
                :pagination-enabled="false"
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
                            <span>{{ item.title }}</span>
                        </router-link>
                    </slide>
                </template>
                <template v-else>
                    등록된 광고가 없습니다.
                </template>
            </carousel>
        </div>
    </div>
</template>

<script>
import {mapGetters, mapState, mapActions} from 'vuex';
import Signup from '@/components/account/Signup.vue';
import Login from '@/components/account/Login.vue';
import { Carousel, Slide } from 'vue-carousel';

export default {
    name: 'Nav',
    components: {
        Signup,
        Login,
        Carousel,
        Slide
    },
    data() {
        return {
            mouseover: false,
            clicked: false,
            spotList: [],
            autoplay: "true",
        };
    },
    computed: {
        ...mapState(['language']),
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
            this.$store.dispatch('logout');
            if(Kakao.Auth.getAccessToken()!=null){
                Kakao.Auth.logout(function(){
                    
                });
            }

            this.$toast('안내', '로그아웃 되었습니다.');
        },
        moveToUserDetail() {
            this.$router.push({
                'name': 'userdetail'
            })
                .catch(() => {});
        },
        setkor() {
            this.$store.commit('kor');
        },
        seteng() {
            this.$store.commit('eng');            
        },
        setch() {
            this.$store.commit('ch');            
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
        async fetchSpotList(){
            const response = await this.$axios({
                method: 'get',
                url: '/spot/banner',
            }).then((res)=>{
                //console.dir(res);
                this.spotList = res.data.data;
            }).catch((error)=>{
                console.log(error.response);
            });
        },
        showCraousel(){
            const showme = document.getElementById('spotCarousel');
            console.dir(showme);
        }
    },
};

</script>

<style scoped>
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
    height: 0;
    width: 0;
    transition-duration: 0.5s;
    z-index:10;
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
    z-index: 10;
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
    z-index:10;
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
    z-index:10;
}
#ddopen li {
    transition-duration: 0.5s;
}
#ddopen li:hover {
    background-color: #bdbdbd;
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
        height: 128px;
        width: 150px;
    }
}
#spot_area {
    position: fixed;
    top: 68px;
    width: 100%;
    z-index:0;
}
.show {
    background-color: #BEDAE5!important;
    display: flex;
    padding: 10px;
}

</style>
