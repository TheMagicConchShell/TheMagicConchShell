<template>
    <div>
        <div id="home">
            <transition name="conversion">
                <span :key="language">{{ $t('title.counselBoard') }}</span>
            </transition>
        </div>
        <div class="d-flex">
            <div id="article">
                <div id="selectbox">
                    <select
                        v-model="currentCategory"
                        @change="fetchCounsel"
                    >
                        <option  
                            id="0"
                            :class="{active: (currentCategory === '전체')}"
                            :value="'전체'"
                        >
                            <span :key="language">{{ $t('board.all') }}</span>
                        </option>
                        <option
                            v-for="category in categories"
                            :id="category.id"
                            :key="category.id"
                            :class="{active: (currentCategory === category.name.ko)}"
                            :value="category.name.ko"
                        >
                            <span :key="language">{{ category.name[language] }}</span>
                        </option>
                    </select>
                </div>
                <ul id="tab">
                    <li
                        id="0"
                        class="cursor-pointer-hover"
                        :class="{active: (currentCategory === '전체')}"
                        @click="fetchCounsel"
                    >
                        {{ $t('board.all') }}
                    </li>
                    <li
                        v-for="category in categories"
                        :id="category.id"
                        :key="category.id"
                        class="cursor-pointer-hover"
                        :class="{active: (currentCategory === category.name.ko)}"
                        @click.stop="fetchCounsel"
                    >
                        {{ category.name[language] }}
                    </li>
                </ul>
                <div>
                    <counsel-board
                        :page="page"
                        :category="currentCategory"
                    />
                    <div>
                        <b-button
                            v-if="nickname"
                            class="button-write"
                            @click="write"
                        >
                            작성
                        </b-button>
                    </div>
                </div>
            </div>
            <transition name="right-side">
                <div
                    v-show="Y"
                    id="category"
                >
                    <ul
                        v-if="categories && categories.length"
                        id="tab-column"
                    >
                        <li
                            id="0"
                            class="cursor-pointer-hover"
                            :class="{active: (currentCategory === '전체')}"
                            @click="fetchCounsel"
                        >
                            {{ $t('board.all') }}
                        </li>
                        <li
                            v-for="category in categories"
                            :id="category.id"
                            :key="category.id"
                            class="cursor-pointer-hover"
                            :class="{active: (currentCategory === category.name.ko)}"
                            @click="fetchCounsel"
                        >
                            {{ category.name[language] }}
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
        ...mapGetters(['language'])
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
    },
    methods: {
        fetchCounsel(e) {
            if (e.target.id === '0') {
                this.currentCategory = '전체';
            } else {
                this.currentCategory = this.$store.getters.categoryNameById(e.target.id);
            }
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

<style lang="scss" scoped>
#home {
  display: flex;
  font-family: sb;
  margin: 30px 0;
  justify-content: space-between;
  font-size: 1.8rem;
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
    margin-bottom: 10px;
    padding: 0;
    width: 100%;
    height: 24px;
    background-color: #ffffff;
}
#selectbox {
    display:none;
}
@media (max-width:992px) {
    #tab {
        display:none
    }
    #selectbox {
        display:flex;

        select {
            padding: 4px 6px;
            margin: 4px 0px;
        }
    }
}
#tab li{
    padding: 4px 6px;
    font-family: sb;
    clear: both;
    width: 70px;
    margin: 0 5px;
    display: inline-block;    
    transition-duration: 0.5s;
    border-radius: 5px;
    color: white;
    background:linear-gradient(to top, #aaaaaa,#bbbbbb,#c0c0c0);
    border:1px solid #dadada;
}
.active {
    background:linear-gradient(to top, #1c3eb4, #154cbd, #1059c6, #1266ce, #1b73d5)!important;
    color:white;
}
#tab li:hover {
    background:linear-gradient(to top, #1c3eb4, #154cbd, #1059c6, #1266ce, #1b73d5);
    color: white
}
#article {
    width: 100%;
}
#tab-column {
    display:flex;
    flex-direction: column;
    align-items: center;
    padding: 0;
    width: 100%;
    background-color: #ffffff;
    border-bottom: 1px solid #ebeaea;
}
#tab-column li{
    clear: both;
    width: 100%;
    display: inline-block;    
    transition-duration: 0.5s;
}
#tab-column li:hover {
    background-color: #0363BA;
    color: white
}
.button-write {
    float:right;
    background-color: #0363BA;
}
.cursor-pointer-hover:hover {
    cursor: pointer;
}
</style>
