<template>
    <div>
        <h2 id="recommendspace">
            현재 메인 글 목록
        </h2>
        <div id="main">
            <b-card 
                v-for="(item, index) in mainlist" 
                :key="index"
                no-body
                class="overflow-hidden mb-3 d-inline"
                footer="Card Footer"
            >
                <template v-slot:header>
                    <div
                        class="ellipsis"
                    > 
                        {{ item.title }}
                    </div>
                </template>
                <b-card-body 
                    class="b-card-body"
                    style="height: 150px;"
                >
                    <b-card-text>
                        <viewer
                            :initial-value="item.content"
                        />
                    </b-card-text>
                </b-card-body>
                <template v-slot:footer>
                    <b-button 
                        variant="link"
                        class="ellipsis"
                        @click.prevent="openDeleteModal(item.no, index)"
                    >
                        메인에서 내리기
                    </b-button>
                </template>
            </b-card>
        </div>
        <infinite-loading
            spinner="waveDots"
            @infinite="infiniteHandler"
        >
            <div
                slot="no-more"
                style="color: rgb(102, 102, 102); font-size: 14px; padding: 10px 0px;"
            />
        </infinite-loading>

        <hr>
        <h2 id="recommendspace">
            추천 영역
        </h2>
        <table
            class="table"
        >
            <colgroup>
                <col>
                <col>
                <col>
                <col>
                <col>
                <col>
                <col>
                <col>
                <col>
                <col>
            </colgroup>
            <thead class="table-info">
                <tr>
                    <th
                        scope="col"
                    >
                        글 번호
                    </th>
                    <th
                        class="text-left"
                        scope="col"
                    >
                        제목
                    </th>
                    <th scope="col">
                        작성자
                    </th>
                    <th scope="col">
                        조회수
                    </th>
                    <th scope="col">
                        추천수
                    </th>
                    <th scope="col">
                        비추천수
                    </th>
                    <th scope="col">
                        답변수
                    </th>
                    <th scope="col">
                        총합
                    </th>
                    <th 
                        scope="col"
                        class="text-left"
                    >
                        작성시간
                    </th>
                    <th>
                        <b-button
                            class="btn-danger"
                            @click.prevent="recommendUpdate"
                        >
                            갱신
                        </b-button>
                    </th>
                </tr>
            </thead>
            <template v-if="recommendList && recommendList.length">
                <tbody>
                    <tr 
                        v-for="(recommend, index) in recommendList"
                        :key="index"
                    >
                        <td>{{ recommend.no }}</td>
                        <td class="text-left">
                            <router-link :to="{path: '/counsel/read/'+ recommend.no}">
                                {{ recommend.title }}
                            </router-link>
                        </td>
                        <td>
                            {{ recommend.writer }}
                        </td>
                        <td>
                            {{ recommend.views }}
                        </td>
                        <td>
                            {{ recommend.likeCount }}
                        </td>
                        <td>
                            {{ recommend.unlikeCount }}
                        </td>
                        <td>
                            {{ recommend.replyCount }}
                        </td>
                        <td>
                            {{ recommend.totalScore }}
                        </td>
                        <td class="text-left">
                            {{ getFormatDate(recommend.writeDate) }}
                        </td>
                        <td>
                            <b-button @click.prevent="openSelectModalFromRecommend(recommend.no, index)">
                                메인 선정
                            </b-button>
                        </td>
                    </tr>
                </tbody>
            </template>
        </table>
        <div v-if="!(recommendList && recommendList.length)">
            추천 가능한 글이 없습니다.
        </div>
        <hr>

        <h2 id="recommendspace">
            고민 목록
        </h2>
        <table
            id="allowed-counsel-list"
            class="table"
        >
            <colgroup>
                <col width="10%">
                <col width="30%">
                <col width="10%">
                <col width="8%">
                <col width="8%">
                <col width="8%">
                <col width="10%">
                <col width="12%">
            </colgroup>
            <thead class="thead-dark">
                <tr>
                    <th
                        scope="col"
                    >
                        글 번호
                    </th>
                    <th
                        class="text-left"
                        scope="col"
                    >
                        제목
                    </th>
                    <th scope="col">
                        작성자
                    </th>
                    <th scope="col">
                        조회수
                    </th>
                    <th scope="col">
                        추천수
                    </th>
                    <th scope="col">
                        비추천수
                    </th>
                    <th 
                        scope="col"
                        colspan="2"
                        class="text-left"
                    >
                        작성시간
                    </th>
                </tr>
            </thead>
            <template v-if="list && list.length">
                <tbody>
                    <tr 
                        v-for="(item, index) in list"
                        :key="index"
                    >
                        <td>{{ item.no }}</td>
                        <td class="text-left">
                            <router-link :to="{path: '/counsel/read/'+ item.no}">
                                {{ item.title }}
                            </router-link>
                        </td>
                        <td>
                            {{ item.writer }}
                        </td>
                        <td>
                            {{ item.views }}
                        </td>
                        <td>
                            {{ item.likeCount }}
                        </td>
                        <td>
                            {{ item.unlikeCount }}
                        </td>
                        <td class="text-left">
                            {{ getFormatDate(item.writeDate) }}
                        </td>
                        <td>
                            <b-button @click.prevent="openSelectModalFromList(item.no, index)">
                                메인 선정
                            </b-button>
                        </td>
                    </tr>
                </tbody>
            </template>
        </table>
        <div v-if="!(list && list.length)">
            메인에 노출 가능한 글이 없습니다.
        </div>
        
        <template v-if="list && list.length">
            <div class="overflow-auto">
                <b-pagination
                    v-model="page"
                    :total-rows="rows"
                    :per-page="perPage"
                    first-text="First"
                    prev-text="Prev"
                    next-text="Next"
                    last-text="Last"
                    align="center"
                    aria-controls="allowed-counsel-list"
                />
            </div>
        </template>

        <b-modal
            id="modal-select-main"
            ref="modal"
            title="메인에 선정하시겠습니까?"
            @ok="handleSelectOk"
        >
            <form 
                ref="form" 
                @submit.stop.prevent="setSelectionPost"
            >
                <b-form-group
                    label="선택 글 번호"
                    label-for="no-input"
                >
                    <b-form-input
                        id="no-input"
                        v-model="selectNo"
                        required
                        readonly
                    />
                </b-form-group>
                <b-form-group
                    :state="descriptionState"
                    label="설명"
                    label-for="description-input"
                    invalid-feedback="설명을 입력해주세요."
                >
                    <b-form-input
                        id="description-input"
                        v-model="descriptionSelect"
                        :state="descriptionState"
                        required
                    />
                </b-form-group>
            </form>
        </b-modal>

        <b-modal
            id="modal-delete-main"
            ref="modal"
            title="해당 글을 메인에서 내리고 히스토리로 이동합니다."
            @ok="handleDeleteOk"
        >
            <form 
                ref="form" 
                @submit.stop.prevent="setSelectionPost"
            >
                <b-form-group
                    label="선택 글 번호"
                    label-for="no-input"
                >
                    <b-form-input
                        id="no-input"
                        v-model="deleteNo"
                        required
                        readonly
                    />
                </b-form-group>
                <b-form-group
                    :state="descriptionState"
                    label="설명"
                    label-for="description-input"
                    invalid-feedback="설명을 입력해주세요."
                >
                    <b-form-input
                        id="description-input"
                        v-model="descriptionDelete"
                        :state="descriptionState"
                        required
                    />
                </b-form-group>
            </form>
        </b-modal>
    </div>
</template>
<script>
import InfiniteLoading from 'vue-infinite-loading';
import { formatDate } from '@/util/format';

export default {
    name: 'CounselSelect',
    components: {
        InfiniteLoading
    },
    data () {
        return {
            selectNo: '',
            descriptionSelect: '',
            descriptionState: null,
            page:1,
            rows: '',
            perPage: '',
            list: [],
            recommendList:[],
            mainlist: [],
            lastNo: 1,
            size: 3,
            deleteNo: '',
            descriptionDelete: '',
            indexDel: '',
            indexSel: '',
            isInList: '',
        };
    },
    watch: {
        page() {
            this.fetchAllowedCounsels(this.page);
        },
    },
    created() {
        this.fetchAllowedCounsels(this.page);
        this.fetchMainPosts(this.size);

        this.$axios({
            url:'/post/recommend/auto',
            method:"get",
        }).then((res)=>{
            if(res.status >= 200 && res.status < 300){
                this.recommendList = res.data.data;
            }
        }).catch((error) => {
        });
    },
    methods: {
        async fetchAllowedCounsels(page) {
            const response = await this.$axios({
                url: `/counsel/post/allowed`,
                method: "get",
                params: {
                    page: page || 1,
                }
            }).then((response) => {
                this.rows = response.data.data.totalElements;
                this.perPage = response.data.data.size;
                this.list = response.data.data.content;
            }).catch((error) => {
            });
        },
        checkFormValidity() {
            const valid = this.$refs.form.checkValidity();
            this.descriptionState = valid;
            return valid;
        },
        openSelectModalFromRecommend(no, index) {
            this.$bvModal.show('modal-select-main');
            this.selectNo = no;
            this.isInList = false;
            this.indexSel = index;
        },
        openSelectModalFromList(no, index) {
            this.$bvModal.show('modal-select-main');
            this.selectNo = no;
            this.isInList = true;
            this.indexSel = index;
        },
        handleSelectOk(bvModalEvt) {
            bvModalEvt.preventDefault();
            this.setSelectionPost();
        },
        setSelectionPost() {
            if (!this.checkFormValidity()) {
                return;
            }
            this.$axios({
                url: `/selection/post`,
                method: "post",
                data: {
                    no: this.selectNo,
                    description: this.descriptionSelect,
                }
            }).then(() => {
                let msg = '메인 글로 선정되었습니다.';
                if(this.isInList) {
                    this.mainlist.push(this.list[this.indexSel]);
                    this.list.splice(this.indexSel, 1);
                } else {
                    this.mainlist.push(this.recommendList[this.indexSel]);
                    this.recommendList.splice(this.indexSel, 1);
                }
                this.$toast('안내', msg);
            }).catch((error) => {
            });

            this.$nextTick(() => {
                this.$bvModal.hide('modal-select-main');
            });
        },
        getFormatDate(date) {
            return formatDate(date, "YYYY.MM.DD");
        },
        async recommendUpdate(){
            await this.$axios({
                url:'/post/recommend',
                method:'get'
            }).then((res)=>{
                if(res.status >= 200 && res.status < 300){
                    this.recommendList = res.data.data;
                }
            }).catch((error) => {
            });
        },
        async fetchMainPosts(size) {
            const response = await this.$axios({
                method: 'get',
                url: `/selection/post`,
                params: {
                    size: size,
                },
            }).then((res) => {
                this.mainlist = res.data.data;
                this.lastNo = this.mainlist[this.mainlist.length-1].no;
            }).catch((error) => {
            });
        },
        infiniteHandler($state) {
            this.$axios({
                method: 'get',
                url: `/selection/post`,
                params: {
                    lastPostNo: this.lastNo,
                    size: this.size,
                },
            }).then((res) => {
                setTimeout(() => {
                    if(res.data.data.length) {
                        this.mainlist = this.mainlist.concat(res.data.data);
                        $state.loaded();
                        this.lastNo = this.mainlist[this.mainlist.length-1].no;
                        // 끝 지정(No more data) - 데이터가 size개 미만이면 
                        if(res.data.data.length / this.size < 1) {
                            $state.complete();
                        }
                    }
                }, 1000);
            }).catch((error) => {
                // 끝 지정(No more data)
                $state.complete();
            });
        },
        openDeleteModal(no, index) {
            this.$bvModal.show('modal-delete-main');
            this.deleteNo = no;
            this.indexDel = index;
        },
        handleDeleteOk(bvModalEvt) {
            bvModalEvt.preventDefault();
            this.removeSelectionPost();
        },
        removeSelectionPost() {
            if (!this.checkFormValidity()) {
                return;
            }

            this.$axios({
                url: `/selection/post`,
                method: "delete",
                data: {
                    no: this.deleteNo,
                    description: this.descriptionDelete,
                }
            }).then(() => {
                let msg = '메인 글에서 내렸습니다.';
                this.$toast('안내', msg);
                this.mainlist.splice(this.indexDel, 1);
            }).catch((error) => {
            });

            this.$nextTick(() => {
                this.$bvModal.hide('modal-delete-main');
            });
        },
    },
};

</script>

<style scoped>
table {
    table-layout: fixed; 
    width: 100%;
}
td {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap; 
}
#recommendspace{
    text-align: center;
}
#main {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    gap: 20px 20px;
}
.ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap; 
}
.card-text {
    width: 100%;
    height: 100%;
    overflow: hidden;
}
</style>