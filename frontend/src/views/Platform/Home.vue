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
            <div 
                v-for="(history,i) in histories"
                id="detail"
                :key="i"
                @mouseover="select(history)"
                @mouseleave="unselect()"
            >
                <div class="skill-card">
                    <router-link
                        :to="{name: 'CounselDetail', params: {no: history.no}}"
                        style="text-decoration: none;"
                    >
                        <header class="skill-card__header">
                            <span id="card_title">{{ history.title }}</span>
                            <viewer
                                :initial-value="history.content"
                            />
                        </header>
                        <section class="skill-card__body">
                            <p class="skill-card__title">
                                <span>
                                    <img
                                        class="skill-card__icon"
                                        :src="history.profileImg? history.profileImg: require(`@/assets/images/default_profile.png`)"
                                    >{{ history.writer }}
                                </span>
                                <span class="skill-card__duration">{{ history.writeDate }}</span>
                            </p>
                            <ul class="skill-card__knowledge">
                                <li>조회수 {{ history.views }}</li>
                                <li>좋아요 {{ history.likeCount }}</li>
                                <li>댓글 {{ history.replies }}</li>
                            </ul>
                        </section>
                    </router-link>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { Carousel3d, Slide } from 'vue-carousel-3d';

import { formatDate } from '@/util/format';

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
                        e.writeDate = formatDate(e.writeDate, 'YYYY.MM.DD HH:mm');
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
                    this.histories = response.data.data.map((h) => {
                        h.writeDate = formatDate(h.writeDate, 'YYYY.MM.DD');
                        h.replies = 1;
                        return h;
                    });
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
  margin: 30px 0;
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
.carousel-3d-slide.right-1, .carousel-3d-slide.left-1 {
    border-radius: 5px;
}
.carousel-3d-slide.right-2, .carousel-3d-slide.left-2 {
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

.skill-card {
  /* width: 15em; */
  background-color: #FFF;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
  cursor: pointer;
  margin-bottom: 20px;
}
.skill-card:hover {
  box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
}
.skill-card:hover .skill-card__header {
  height: 40px;
}
/* .skill-card:hover .skill-card__header .skill-card__icon {
  -webkit-transform: translateX(-50%) translateY(-50%) scale(3);
          transform: translateX(-50%) translateY(-50%) scale(3);
} */
#card_title {
    font-family: sb;
    font-size:120%;
}
.skill-card:hover .skill-card__body {
  height: 180px;
}
.skill-card:hover .skill-card__body .skill-card__knowledge li {
  -webkit-transform: translateX(0);
          transform: translateX(0);
  -webkit-transition-timing-function: cubic-bezier(0, 1.5, 1, 1);
          transition-timing-function: cubic-bezier(0, 1.5, 1, 1);
  -webkit-transition-duration: 0.5s;
          transition-duration: 0.5s;
}
.skill-card:hover .skill-card__body .skill-card__knowledge li:nth-child(1) {
  -webkit-transition-delay: 0.25s;
          transition-delay: 0.25s;
}
.skill-card:hover .skill-card__body .skill-card__knowledge li:nth-child(2) {
  -webkit-transition-delay: 0.5s;
          transition-delay: 0.5s;
}
.skill-card:hover .skill-card__body .skill-card__knowledge li:nth-child(3) {
  -webkit-transition-delay: 0.75s;
          transition-delay: 0.75s;
}
.skill-card:hover .skill-card__body .skill-card__knowledge li:nth-child(4) {
  -webkit-transition-delay: 1s;
          transition-delay: 1s;
}
.skill-card:hover .skill-card__body .skill-card__knowledge li:nth-child(5) {
  -webkit-transition-delay: 1.25s;
          transition-delay: 1.25s;
}
.skill-card:hover .skill-card__body .skill-card__knowledge li:nth-child(6) {
  -webkit-transition-delay: 1.5s;
          transition-delay: 1.5s;
}
.skill-card .skill-card__header {
  position: relative;
  padding: 20px;
  /* background-color: #eb6c3f; */
  height: 160px;
  margin: -20px -20px 0 -20px;
  -webkit-transition: height 0.5s;
  transition: height 0.5s;
  overflow: hidden;
}
.skill-card__icon {
    width: 20px;
    border-radius: 20px;
}
/* .skill-card .skill-card__header{
  position: absolute;
  top: 50%;
  left: 50%;
  -webkit-transform: translateY(-50%) translateX(-50%);
          transform: translateY(-50%) translateX(-50%);
  display: block;
  width: 60%;
  max-height: auto;
  -webkit-transition: -webkit-transform 0.5s;
  transition: -webkit-transform 0.5s;
  transition: transform 0.5s;
  transition: transform 0.5s, -webkit-transform 0.5s;
} */
.skill-card .skill-card__body {
  height: 90px;
  padding: 20px 20px 20px 20px;
  -webkit-transition: height 0.5s;
  transition: height 0.5s;
  overflow: hidden;
  display:flex;
  flex-direction: column;
  align-items: flex-end;
  background-color: #ededed;
}
.skill-card .skill-card__body .skill-card__title {
    font-family: sb;
    display:flex;
    flex-direction: column;
    margin:0;
}
.skill-card .skill-card__body .skill-card__duration {
  color: #9E9E9E;
  font-style: italic;
}
.skill-card .skill-card__body .skill-card__knowledge {
  color: #424242;
}
.skill-card .skill-card__body .skill-card__knowledge li {
    font-family: sb;
    color: #6B799E;
  margin-top: 5px;
  -webkit-transition-duration: 0;
          transition-duration: 0;
  -webkit-transition-delay: 0.5s;
          transition-delay: 0.5s;
  -webkit-transform: translateX(-550%);
          transform: translateX(-550%);
}
</style>