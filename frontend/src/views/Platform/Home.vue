<template>
    <div>
        <div id="home">
            <div class="d-flex flex-column-reverse">
                <transition name="conversion">
                    <span
                        v-if="language==='ko'"
                        key="1"
                    >
                        <p>금주의 싸피고둥이들</p>
                    </span>
                    <span
                        v-else
                        key="2"
                    >Wise Conches of the Week </span>
                </transition>
            </div>
        </div>
        <!-- 금주 -->
        <div
            id="thisweek"
        >
            <swiper
                ref="mySwiper"
                class="swiper"
                :options="swiperOption"
                style="overflow:hidden"
            >
                <swiper-slide
                    v-for="post in list"
                    :key="post.no"
                    style="overflow: auto"
                >
                    <router-link
                        :to="{name: 'CounselDetail', params: {no: post.no}}"
                        style="text-decoration: none;"
                    >
                        <h3>{{ post.title }}</h3>
                        <p class="d-flex">
                            written by {{ post.writer }}
                        </p>
                        <viewer
                            :initial-value="post.content"
                        />
                    </router-link>
                </swiper-slide>
                <div
                    slot="pagination"
                    class="swiper-pagination"
                />
                <div
                    slot="button-prev"
                    class="swiper-button-prev"
                />
                <div
                    slot="button-next"
                    class="swiper-button-next"
                />
            </swiper>          
            <!-- <div id="mainpost">
                <transition name="fade-in">
                    <div
                        v-if="nowshowing"
                        key="1"
                    >
                        <h3>{{ nowshowing.title }}</h3>
                        <p class="d-flex">
                            written by {{ nowshowing.writer }}
                        </p>
                        <viewer
                            :initial-value="nowshowing.content"
                        />
                    </div>
                    <div
                        v-else
                        key="2"
                    >
                        이런... 사이트가 망해서 고민이 없습니다ㅠㅠ
                    </div>
                </transition> -->
        </div>
        <!-- <div 
                id="sidepost" 
                v-infinite-scroll="loadMore" 
                infinite-scroll-disabled="busy" 
                infinite-scroll-distance="3"
            >
                <div
                    v-for="post in list"
                    :key="post.no"
                    class="w-100 m-0 p-0 cursor"
                    @click="pageswap(post.no)"
                >
                    <div
                        v-if="post.no===nowshowing.no"
                        id="selected"
                    >
                        <p style="text-overflow:ellipsis">
                            {{ post.title }}
                        </p>
                    </div>
                    <div
                        v-if="post.no!==nowshowing.no"
                        id="spcontent"
                    >
                        <p>{{ post.title }}</p>
                    </div>
                </div>
            </div>   -->

        <div id="home">
            <div class="d-flex flex-column-reverse">
                <transition name="conversion">
                    <span
                        v-if="language==='ko'"
                        key="1"
                    >지난 대나무숲</span>
                    <span
                        v-else
                        key="2"
                    >Spilled beans</span>
                </transition>
            </div>
        </div>

        <!-- 지난주 -->
        <div
            v-for="history in histories"
            id="history"
            :key="history.no"
            class="cursor"       
        >
            <b-card
                id="detail"
                class="mb-3"
            >
                <router-link
                    :to="{name: 'CounselDetail', params: {no: history.no}}"
                    style="text-decoration: none;"
                >
                    <b-card-text>
                        <div id="answerheader">
                            <h3>{{ history.title }}</h3>
                            <viewer
                                :initial-value="history.content"
                            />
                        </div>
                    </b-card-text>
                </router-link>
            </b-card>
            <b-card
                class="mb-3"
            >
                <b-card-text>
                    <div id="answerheader">
                        빈카드
                    </div>
                </b-card-text>
            </b-card>
            <b-card
                class="mb-3"
            >
                <b-card-text>
                    <div id="answerheader">
                        빈카드
                    </div>
                </b-card-text>
            </b-card>
            <b-card
                class="mb-3"
            >
                <b-card-text>
                    <div id="answerheader">
                        빈카드
                    </div>
                </b-card-text>
            </b-card>
        </div>
    </div>
</template>

<script>
import {mapState} from 'vuex';
import axios from 'axios';
import { Swiper, SwiperSlide } from 'vue-awesome-swiper';
const api = 'http://i3a403.p.ssafy.io:8399';
var count = 0;

export default {
    name: 'Home',
    components: {
        Swiper, SwiperSlide
    },
    data() {
        return {
            nowshowing: null,            
            lastNo: 1,
            size: 5,
            list: [],
            histories: [],
            busy: false,
            p_img: '',
            /* swiper */
            swiperOption: {
                effect: 'coverflow',
                grabCursor: true,
                centeredSlides: true,
                slidesPerView: 3,
                spaceBetween: 30,
                loop: true, 
                coverflowEffect: {
                    rotate: 50,
                    stretch: 0,
                    depth: 100,
                    modifier: 1,
                    slideShadows : true
                },
                mousewheel: true,
                keyboard: {
                    enabled: true,
                },
                pagination: {
                    el: '.swiper-pagination',
                    clickable: true
                },
                navigation: {
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev'
                }
            }
        };
    },
    computed: {
        ...mapState(['language']),
        swiper() {
            return this.$refs.mySwiper.swiper;
        }
    },
    created() {
        this.fetchPosts(this.size);
        this.fetchHistory(this.size);
    },
    methods: {
        loadMore() {
            this.busy = true;
            setTimeout(() => {
                while (count < this.pageCount) {
                    this.fetchPosts(count++);
                }
                this.busy = false;
            }, 1000);
        },
        async fetchPosts(size) {
            const response = await this.$axios.get(api + '/selection/post', { params: { size: size }})
                .catch(() => console.log('catch notices'));
            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.list = response.data.data;
                    this.lastNo = this.list[this.list.length-1].no;
                }
            }
            this.nowshowing = this.list[0];
        },
        async fetchHistory(size) {
            const response = await this.$axios.get(api + '/selection/history', {params: {size: size}})
                .catch(() => console.log('catch notices'));
            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.histories = response.data.data;
                    this.lastNo = this.histories[this.histories.length-1].no;
                }
            }
        },
        async fetchProfileimage(nickname) {
            const response = await this.$axios.get(api + '/user/detail', {params: nickname})
                .catch(() => console.log('catch notices'));
            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.p_img = response.data.data.profile_image;
                }
            }
        },
        async pageswap(no) {
            const response = await this.$axios({
                url: `counsel/post/post-no`,
                method: "get",
                params: {
                    no: no
                }
            }).catch(() => {
                console.log("catch notices");
            });
            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.pageCount = response.data.data.totalPages;
                    this.nowshowing = response.data.data.post;
                }
            };
        },
        infiniteHandler($state) {
            axios.get((api + '/counsel/post', {
                params: {
                    page: this.page,
                },
            }).then(({ response }) => {
                console.log(response);
                if (response.data.data.content / 3 === 0) {
                    this.page += 1;
                    fetchPosts(this.page);
                    $state.loaded();
                } else {
                    $state.completed();
                }
            })
            );},
    },
};
</script>

<style scoped>
#home {
  display: flex;
  margin: 30px 0;
  justify-content: space-between;
  font-size: 30px;
}

#thisweek {
    width: 100%;
    height: 80vh;
    padding-top: 50px;
    padding-bottom: 50px;
}
#mainpost {
    border-radius: 0 5px 5px 0;
    height: 100%;
    border-top: 1px solid #cacaca;
    border-left: 1px solid #cacaca;
    border-right: 1px solid #cacaca;
    padding: 10px;
    background-size: cover;
    background-color: #ffffff;
    text-overflow: ellipsis;
}
#mainpost div {
    max-width: 100%;
    height: auto;
}
#sidepost {
    display: flex;
    height: 24px;
    white-space: nowrap;
}
#sidepost div {
    display: flex;
    justify-content: center;
    color: black;
    background-color: #A6C2CE;
    border-radius: 0 0 5px 5px;
}
#spcontent {
    width: 100%;
    text-overflow:hidden;
    border-left: 1px solid #cacaca;
    border-right: 1px solid #cacaca;
    border-bottom: 1px solid #cacaca;
}
#spcontent:hover {
    background-color: #ffffff;
    transition-duration: 0.5s;
    border-radius: 0 0 5px 5px;
}
#selected {
    width: 100%;
    background-color: #ffffff!important;
    border-left: 1px solid #cacaca;
    border-right: 1px solid #cacaca;
    border-bottom: 1px solid #cacaca;
    border-radius: 0 0 5px 5px;
}
#history {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    gap: 20px 20px;
}
#detail {
    border: 1px solid #cacaca;
    box-shadow: 10px 10px 10px #9e9e9e;
}
.swiper {
    height: 100%;
    width: 100%;
}
.swiper-slide {
      padding:20px;
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: center;
      width: 300px;
      height: 100%;
      text-align: center;
      font-weight: bold;
      background-position: center;
      background-size: cover;
      border: 1px solid #cacaca;
      box-shadow: 10px 10px 10px #9e9e9e;
}
</style>