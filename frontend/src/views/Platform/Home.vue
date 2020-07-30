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
            <div id="mainpost">
                <div
                    v-if="nowshowing"
                >
                    <h3>{{ nowshowing.title }}</h3>
                    <p>written by {{ nowshowing.writer }}</p>
                    <div v-html="`${nowshowing.content}`"></div>
                </div>
                <div v-else>
                    이런... 사이트가 망해서 고민이 없습니다ㅠㅠ
                </div>
            </div>
            <div id="sidepost">
                <b-jumbotron
                    v-for="post in list"
                    :key="post.no"
                    fluid
                    class="w-100 m-0 border"
                    @click="pageswap(post.no)"
                >
                    <h4>{{ post.title }}</h4>
                    <p>written by {{ post.writer }}</p>
                </b-jumbotron>
                <infinite-loading @infinite="infiniteHandler" />
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

export default {
    name: 'Home',
    data() {
        return {
            nomouse: true,
            nowshowing: null,
            /* 무한 스크롤 데이터 */
            page: 0,
            pageCount: 1,
            list: [],
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
    },
    methods: {
        mouseover() {
            this.nomouse = false;
        },
        mouseout() {
            this.nomouse = true;
        },
        async fetchPosts(page) {
            const response = await this.$axios({
                url: `counsel/post`,
                method: "get",
                headers: {
                    nickname: sessionStorage.getItem('nickname') || '',
                },
                params: {
                    page: page || 1,
                }
            }).catch(() => {
                console.log("catch notices");
            });
            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.pageCount = response.data.data.totalPages;
                    this.list = response.data.data.content;
                }
            }
            this.nowshowing = this.list[0];
        },
        async pageswap(no) {
            console.log(no);
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
        /* 무한스크롤 메서드 */
        infiniteHandler($state) {
            axios.get('', {
                params: {
                    page: this.page,
                },
            })
                .then(({ data }) => {
                    if (data.hits.length) {
                        this.page += 1;
                        this.list.push(...data.hits);
                        $state.loaded();
                    } else {
                        $state.complete();
                    }
                });
        },
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
    border-radius: 5px;
    width: 80%;
    height: 100%;
    background-image: url(../../assets/images/parchment.jpg);
    background-size: cover;
    margin: 0 20px;
    padding: 100px;
    overflow: auto;
}
#sidepost {
    width: 20%;
    height: 100%;
    overflow: auto;
    border-radius: 5px;
}
.jumbotron {
    background-color: #A6C2CE;
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
