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
            </swiper>          
        </div>
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
            id="history"
            class="cursor"       
        >
            <b-card
                v-for="history in histories"
                id="detail"
                :key="history.no"
                class="mb-3 d-inline"
                @mouseover="select(history)"
                @mouseleave="unselect()"
            >
                <router-link
                    :to="{name: 'CounselDetail', params: {no: history.no}}"
                    style="text-decoration: none;"
                >
                    <b-card-text>
                        <div id="history_detail">
                            <p>{{ history.title }}</p>
                            <transition name="open">
                                <viewer
                                    v-if="nowshowing===history"
                                    id="history_content"
                                    :initial-value="history.content"
                                />
                            </transition>
                        </div>
                    </b-card-text>
                </router-link>
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
        Swiper, 
        SwiperSlide,
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
                grabCursor: true,
                loop: true,
                slidesPerView: 3,
                centeredSlides: true,
                breakpoints: {
                    992: {
                        slidesPerView: 5,
                    }
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
            return this.$refs.mySwiper;
        },
    },
    created() {
        this.fetchPosts(this.size);
        this.fetchHistory(this.size);
    },
    methods: {
        select(selected) {
            this.nowshowing=selected;
        },
        unselect() {
            this.nowshowing=false;
        },
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
  padding-top: 30px;
  justify-content: space-between;
  font-family: sb;
}
#thisweek {
    width: 100%;
    height: 30vh;
    margin-bottom: 50px;
}
#history {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    gap: 20px 20px;
}
.swiper {
    height: 100%;
    width: 100%;
    border: 1px solid;
}
.swiper-slide {
      padding:20px;
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: start;
      width: 300px;
      height: 100%;
      background-color: white;
      box-shadow: 10px 10px 10px #9e9e9e;
}
.swiper-slide a {
    color: black;
}

@media (max-width: 992px) {
    #home {
        font-size: 22.5px;
    }
    #history {
        grid-template-columns: 1fr 1fr;
    }
}
#detail {
    border: 1px solid #cacaca;
    box-shadow: 10px 10px 10px #9e9e9e;
    height: 80px;
    -webkit-transition: all 0.5s ease-in-out;
    transition: all 0.5s ease-in-out;
    z-index: 0;
}
#detail:hover {
    height: 100%;
    z-index: 1;
}
#detail:hover #history_detail p {
    border-bottom: 1px solid;
}
#history_detail {
    color: black;
}
#history_content {
    position: absolute;
}
</style>