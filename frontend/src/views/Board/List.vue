<template>
    <div>
        <div id="home">
            <span v-if="language==='ko'">고민게시판</span>
            <span v-else>Counsel</span>
        </div>
        <div class="d-flex">
            <div id="article">
                <ul id="tab">
                    <li
                        class="cursor-pointer-hover"
                        :class="{active: (currentCategory === '전체')}"
                        @click="fetchCounsel"
                    >
                        <span>전체</span>
                    </li>
                    <li
                        v-for="category in categories"
                        :key="category.name"
                        class="cursor-pointer-hover"
                        :class="{active: (currentCategory === category.name)}"
                        @click="fetchCounsel"
                    >
                        <span>{{ category.name }}</span>
                    </li>
                </ul>
                <div>
                    <counsel-board
                        :page="page"
                        :category="currentCategory"
                    />
                </div>
                <div />
                <button
                    v-if="nickname"
                    class="button-write"
                    @click="write"
                >
                    작성
                </button>
            </div>
            <transition name="right-side">
                <div
                    v-show="Y"
                    id="category"
                >
                    고민 카테고리
                    <ul>
                        <li>
                            전체
                        </li>
                        <li
                            v-for="category in categories"
                            :key="category.id"
                        >
                            {{ category.name }}
                        </li>
                    </ul>
                </div>
            </transition>
        </div>
    </div>
</template>

<script>
import { mapState, mapGetters } from 'vuex';
import CounselBoard from '@/views/counsel/CounselBoard.vue';

export default {
    name: 'List',
    components: {
        CounselBoard,
    },
    data: () => ({
        page: 1,
        pageCount: 1,
        Y: false,
        currentCategory: '전체',
    }),
    computed: {
        ...mapGetters(['categories', 'nickname']),
        ...mapState(['language'])
    },
    watch: {
        $route() {
            this.page = this.$route.query.page;
        },
    },
    created() {
        this.$store.dispatch('fetchCategories');
    },
    mounted() {
        this.$nextTick(window.addEventListener('scroll', this.detectWindowScrollY));
        // window.addEventListener('scroll', this.detectbottom);
    },
    methods: {
        fetchCounsel(e) {
            console.log(e.target.innerText);
            this.currentCategory = e.target.innerText;
            this.page = 1;
        },
        write() {
            this.$router.push({
                name: 'counselregist'
            });
        },
        detectWindowScrollY() {
            this.Y = window.scrollY > 100;
        },
    }
};
</script>

<style scoped>
.active {
    background: #A6C2CE;
}
#home {
  display: flex;
  margin: 30px 0;
  justify-content: space-between;
  font-size: 30px;
}
#category {
    position: fixed;
    right: 10px;
    width: 10%;
    border: 1px solid;
    background-color: #ffffff;
}
#tab {
    display:flex;
    align-items: center;
    padding: 0;
    width: 100%;
    height: 24px;
    font-size: 16px;
    background-color: #ffffff;
}
#tab li{
    clear: both;
    min-width: 12.5%;
    display: inline-block;
    border: 1px solid;
    
}
#article {
    margin-left: 20px;
    width: 100%;
}

.button-write {
    float: right;
}
.cursor-pointer-hover:hover {
    cursor: pointer;
}
</style>
