<template>
    <div>
        <div id="home">
            <div class="d-flex flex-column-reverse">
                금주의 싸피고둥이들
            </div>
        </div>
        <!-- 금주 -->
        <div
            id="bloglist"
            class="d-flex"
        >
            <div 
                id="sidepost" 
                v-infinite-scroll="loadMore" 
                infinite-scroll-disabled="busy" 
                infinite-scroll-distance="3"
            >
                <div
                    v-for="post in list"
                    :key="post.no"
                    fluid
                    class="w-100 m-0 border cursor"
                    @click="pageswap(post.no)"
                >
                    <div
                        v-if="post.no===nowshowing.no"
                        id="selected"
                    >
                        <h4>{{ post.title }}</h4>
                    </div>
                    <div v-else>
                        <h4>{{ post.title }}</h4>
                    </div>
                </div>
                <!-- <infinite-loading @infinite="infiniteHandler" /> -->
            </div>            
            <div id="mainpost">
                <div
                    v-if="nowshowing"
                >
                    <router-link :to="{path: `/counsel/read/${nowshowing.no}`}">
                        <h3>{{ nowshowing.title }}</h3>
                    </router-link>
                    <p class="d-flex">
                        written by {{ nowshowing.writer }}
                    </p>
                    <!-- eslint-disable -->
                    <viewer
                        v-html="nowshowing.content"
                    />
                    <!-- eslint-disable -->
                </div>
                <div v-else>
                    이런... 사이트가 망해서 고민이 없습니다ㅠㅠ
                </div>
            </div>
        </div>

        <div id="home">
            <div class="d-flex flex-column-reverse">
                지난 대나무숲
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
#click {
  animation: motion 0.5s linear 0s infinite alternate;
}

#bloglist {
    width: 100%;
    height: 70vh;
}
#mainpost {
    border-radius: 0 5px 5px 0;
    width: 80%;
    height: 100%;
    border: 1px solid #cacaca;
    background-size: cover;
    padding: 100px;
    overflow: auto;
}
#mainpost div {
    max-width: 100%;
    height: auto;
}
#sidepost {
    width: 20%;
    height: 100%;
    overflow: auto;
    border-radius: 5px 0 0 5px;
    border: 1px solid #cacaca;
    background-color: #BEDAE5;
}
#sidepost div {
    display: flex;
    width:100%;
    background-color: #A6C2CE;
    border: 1px solid #cacaca;
    color: black;
}
#sidepost div div{
    border: none;
    padding: 10px;
}
#selected {
    background-color: #6B799E!important;
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
a {
    color: unset;
}
a:hover {
    text-decoration: unset;
}
</style>
