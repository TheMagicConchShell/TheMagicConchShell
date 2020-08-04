<template>
    <div>
        <div id="home">
            <div class="d-flex flex-column-reverse">
                <transition name="conversion">
                    <span
                        v-if="language==='ko'"
                        key="1"
                    >금주의 싸피고둥이들</span>
                    <span
                        v-else
                        key="2"
                    >Wise Conches of the Week </span>
                </transition>
            </div>
        </div>
        <!-- 금주 -->
        <div
            id="bloglist"
        >          
            <div id="mainpost">
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
                </transition>
            </div>
            <div 
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
                <!-- <infinite-loading @infinite="infiniteHandler" /> -->
            </div>  
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
            id="bloglist"
            class="cursor"
        >
            <b-card
                img-src="https://placekitten.com/300/300"
                img-alt="Card image"
                img-left
                class="mb-3"
            >
                <b-card-text>
                    <div id="answerheader">
                        답변들
                    </div>
                </b-card-text>
            </b-card>
        </div>
    </div>
</template>

<script>
import {mapState} from 'vuex';
import axios from 'axios';
const api = 'http://i3a403.p.ssafy.io:8399';
var count = 0;

export default {
    name: 'Home',
    data() {
        return {
            nomouse: true,
            nowshowing: null,            
            pageCount: 1,
            page: 0,
            list: [],
            busy: false,
        };
    },
    computed: {
        ...mapState(['language'])
    },
    watch: {
        '$route'() {
            this.page = this.$route.query.page;
        },
        page() {
            this.fetchPosts(this.page);
        },
    },
    created() {
        this.page = Number.parseInt(this.$route.query.page);
        this.fetchPosts();
    },
    updated() {
        this.$nextTick(
            setTimeout(nextpage(this.nowshowing.no), 2000)
        );
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
        mouseover() {
            this.nomouse = false;
        },
        mouseout() {
            this.nomouse = true;
        },
        async fetchPosts(page) {
            const response = await this.$axios.get(api + '/counsel/post', { params: { page: page || 1 }, headers: { nickname: '' }})
                .catch(() => console.log('catch notices'));
            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.pageCount = response.data.data.totalPages;
                    this.list = response.data.data.content;
                }
            }
            this.nowshowing = this.list[0];
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
        nextpage(no) {
            if (no+1 <= this.list.length) {
                this.nowshowing = this.list[no+1]; } else {
                this.nowshowing = this.list[0];
            }
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
#bigbutton {
  color: white;
  padding: 10px;
  background: linear-gradient( 0, #9C8F96, #ccbbc4);
  width: 20%;
  border-radius: 10px;
  text-decoration: none;
}

#bloglist {
    width: 100%;
    height: 30vh;
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
#slide {
    max-height: auto;
    max-width: auto;
}
#answerheader {
    height: 100px;
}
#slide {
    border: 1px solid;
    width: 50%;
}
</style>
