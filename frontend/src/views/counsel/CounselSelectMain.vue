<template>
    <div>
        <h1 style="border: gray 1px solid">
            추천 영역..
        </h1>

        <template v-if="list && list.length">
            <table
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
                <tbody>
                    <tr 
                        v-for="item in list"
                        :key="item.no"
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
                            <b-button @click.prevent="openSelectMainModal(item.no)">
                                메인 선정
                            </b-button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </template>

        <template v-else>
            <td>
                로딩중입니다..
            </td>
        </template>
        
        <template v-if="list && list.length">
            <CounselSelectMainPaginate
                :current="page"
                :last="pageCount"
            />
        </template>

        <b-modal
            id="modal-select-main"
            ref="modal"
            title="메인에 선정하시겠습니까?"
            @ok="handleOk"
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
                        v-model="no"
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
                        v-model="description"
                        :state="descriptionState"
                        required
                    />
                </b-form-group>
            </form>
        </b-modal>
    </div>
</template>
<script>
import CounselSelectMainPaginate from "@/components/CounselSelectMainPaginate";
import moment from 'moment';

export default {
    name: 'CounselSelectMain',
    components: {
        CounselSelectMainPaginate,
    },
    data () {
        return {
            no: '',
            description: '',
            descriptionState: null,
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
            this.fetchAllowedCounsels(this.page);
        },
    },
    created() {
        this.page = Number.parseInt(this.$route.query.page);
    },
    mounted() {
    },
    methods: {
        async fetchAllowedCounsels(page) {
            const response = await this.$axios({
                url: `/counsel/post/allowed`,
                method: "get",
                params: {
                    page: page || 1,
                }
            }).catch((error) => {
                console.log(error.response);
            });

            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.pageCount = response.data.data.totalPages;
                    this.list = response.data.data.content;
                }
            }
        },
        checkFormValidity() {
            const valid = this.$refs.form.checkValidity();
            this.descriptionState = valid;
            return valid;
        },
        openSelectMainModal(no) {
            this.$bvModal.show('modal-select-main');
            this.no = no;
        },
        handleOk(bvModalEvt) {
            // Prevent modal from closing
            bvModalEvt.preventDefault();
            // Trigger submit handler
            this.setSelectionPost();
        },
        setSelectionPost() {
            if (!this.checkFormValidity()) {
                return;
            }
            ai({
                url: `/selection/post`,
                method: "post",
                data: {
                    no: this.no,
                    description: this.description,
                }
            }).then(() => {
                let msg = '메인 글로 선정되었습니다.';
                this.$toast('안내', msg);
            }).catch((error) => {
                console.log(error.response);
            });

            this.$nextTick(() => {
                this.$bvModal.hide('modal-select-main');
            });
        },
        getFormatDate(date) {
            return moment(new Date(date)).format("YYYY.MM.DD");
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
</style>