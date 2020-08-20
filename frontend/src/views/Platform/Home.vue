<template>
    <div>
        <div id="home">
            <div class="d-flex flex-column-reverse">
                <transition name="conversion">
                    <span :key="language">
                        <p class="d-flex m-0">
                            {{ $t('home.thisWeek') }}
                        </p>
                        <small id="exp">
                            {{ $t('home.message', {count: list.length}) }}<span>.</span><span>.</span><span>.</span>
                        </small>
                    </span>
                </transition>
            </div>  
        </div>
        <!-- 금주 -->
        <div
            id="thisweek"
        >
            <Carousel3d
                :key="list.length"
                :autoplay="true"
                :autoplay-timeout="10000"
                :perspective="0"
                :space="400"
                :display="5"
                :controls-visible="true"
            >
                <Slide
                    v-for="(post,i) in list"
                    :key="post.no"
                    :index="i"
                >
                    <router-link
                        :to="{name: 'CounselDetail', params: {no: post.no}}"
                        style="text-decoration: none;"
                    >
                        <div id="slide">
                            <div id="slideheader">
                                <p style="font-family:sb">
                                    {{ post.title }}
                                </p>
                                <p class="d-flex justify-content-end align-items-center">
                                    <img
                                        :src="post.profileImg? post.profileImg: require(`@/assets/images/default_profile.png`)"
                                        style="width: 5%;max-height: auto; border-radius: 20px;"
                                    >{{ post.writer }}
                                </p>
                                <small class="d-flex justify-content-end">{{ post.writeDate }}</small>
                            </div>
                            <div id="slidebody">
                                <viewer
                                    :initial-value="post.content"
                                />
                            </div>
                            <div id="slidefooter">
                                더보기..
                            </div>
                        </div>
                    </router-link>
                </Slide>
            </Carousel3d>      
        </div>
        <div id="home">
            <div class="d-flex flex-column-reverse">
                <transition name="conversion">
                    <span :key="language">{{ $t('home.history') }}</span>
                </transition>
            </div>
        </div>

        <!-- 지난주 -->
        <div
            id="history"
            class="cursor"       
        >
            <b-card
                v-for="(history,i) in histories"
                id="detail"
                :key="i"
                class="mb-3 d-inline"
                @mouseover="select(history)"
                @mouseleave="unselect()"
            >
                <router-link
                    :to="{name: 'CounselDetail', params: {no: history.no}}"
                    style="text-decoration: none;"
                >
                    <b-card-text>
                        <p>{{ history.title }}</p>
                        <div id="history_detail">                           
                            <div
                                v-if="nowshowing===history"
                                id="history_content"
                            >
                                조회 {{ history.views }} 좋아요 {{ history.likeCount }} 댓글 {{ history.replies }}
                            </div>
                        </div>
                    </b-card-text>
                </router-link>
            </b-card>
        </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { Carousel3d, Slide } from 'vue-carousel-3d';
import moment from 'moment';

export default {
    name: 'Home',
    components: { Carousel3d, Slide },
    data() {
        return {
            count: 0,
            nowshowing: null,          
            size: 1000,
            list: [],
            histories: [],
            replies: 0,
        };
    },
    computed: {
        ...mapGetters(['language']),
    },
    created() {
        this.fetchPosts(this.size);
        this.fetchHistory(this.size);
    },
    methods: {
        formatDate(writeDate) {
            return moment(new Date(writeDate)).format("YYYY.MM.DD HH:mm");
        },
        select(selected) {
            this.nowshowing=selected;
        },
        unselect() {
            this.nowshowing=false;
        },
        async fetchPosts(size) {
            const response = await this.$axios({
                url: '/selection/post',
                method: 'get',
                params: {
                    size: size,
                },
            })
                .catch(() => {
                });

            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.list = response.data.data.map((e) => {
                        e.writeDate= this.formatDate(e.writeDate);
                        return e;
                    });
                }
            }
        },
        async fetchHistory(size) {
            const response = await this.$axios({
                url: '/selection/history',
                method: 'get',
                params: {
                    size: size,
                },
            })
                .catch(() => {
                });

            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.histories = response.data.data;
                    this.histories.forEach((e) => {
                        this.fetchreplies(e.no)
                            .then((length) => {
                                e.replies = length;
                            })
                            .catch(() => {});
                    });
                }
            }
        },
        fetchreplies(number) {
            return new Promise((resolve, reject) => {
                this.$axios({
                    url: '/counsel/post/post-no',
                    method: 'get',
                    params: {
                        no: number,
                    },
                })
                    .then((response) => {
                        if (response.status >= 200 && response.status < 300) {
                            resolve(response.data.data.replies.length);
                        } else {
                            reject(error);
                        }
                    })
                    .catch((error) => {
                        reject(error);
                    });
            });
        },
        async pageswap(no) {
            const response = await this.$axios({
                url: `counsel/post/post-no`,
                method: "get",
                params: {
                    no: no
                }
            })
                .catch(() => {
                });

            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.pageCount = response.data.data.totalPages;
                    this.nowshowing = response.data.data.post;
                }
            };
        },
    },
};
</script>

<style scoped>
#home {
  display: flex;
  margin: 30px 0 0 0;
  padding-top: 30px;
  justify-content: space-between;
  font-family: sb;
  font-size: 120%;
}
#exp {
    font-family: md;
    display:flex;
    margin: 0 0 30px 0 ;
}
#exp span {
    animation-name: blink;
    animation-duration: 1.4s;
    animation-iteration-count: infinite;
    animation-fill-mode: both;
}
#exp span:nth-child(2) {
    animation-delay: .2s;
}
#exp span:nth-child(3) {
    animation-delay: .4s;
}
#thisweek {
    width: 100%;
    height: 40vh;
    margin-bottom: 50px;
}
#history {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    gap: 20px 20px;
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
    border: 1px solid #f1f1f1;
    box-shadow: 10px 10px 10px #9e9e9e;
    height: 80px;
    -webkit-transition: all 0.5s ease-in-out;
    transition: all 0.5s ease-in-out;
    z-index: 0;
}
#detail:hover {
    height: 100%;
    z-index: 1;
    overflow:hidden;
}
#history_detail {
    color: black;
}
#history_content {
    position: absolute;
    overflow: hidden;
}

/* 슬라이드 */
.carousel-3d-container {
    height: 300px !important;
}
.carousel-3d-slide {
    padding: 5px;
    border: 1px solid #e7e7e7;
    border-radius: 5px;
    background-color: white;
    box-shadow: 10px 10px 10px #9e9e9e;
}
.carousel-3d-slide.current:hover {

}
.carousel-3d-slide.right-1, .carousel-3d-slide.left-1 {
    /* background-color: #f5f5f5; */
    border-radius: 5px;
}
.carousel-3d-slide.right-2, .carousel-3d-slide.left-2 {
    /* background-color: #e7e7e7; */
    border-radius: 5px;
}
#slidefooter {
    position: absolute;
    display:flex;
    flex-direction: row-reverse;
    align-items: flex-end;
    z-index:3;
    border: none;
    bottom: 0;
    right: 0;
    width: 100%;
    height: 20%;
    font-weight: bold;
    background-color: white;
    opacity: 0.8;
}
a:hover {
    color: #777777;
}
</style>