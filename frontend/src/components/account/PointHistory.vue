<template>
    <div>
        <template v-if="pointList && pointList.length">
            <div>
                <h3>
                    {{ $t('account.info.point.current') }}: {{ totalPoint }}
                </h3>
            </div>
            <b-list-group
                id="point-list"
                :per-page="perPage"
                :current-page="page"
            >
                <b-list-group-item
                    v-for="item in pointList"
                    :key="item.id"
                    class="flex-column align-items-start"
                >
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="item">
                            {{ item.reason }}
                        </h5>
                        <h3 
                            :class="(item.change > 0)? 'plus-point item' : 'minus-point item'"
                        >
                            {{ getPointSign(item.change) }}
                        </h3>
                        <p class="item">
                            {{ getFormatDate(item.stamp) }}
                        </p>
                    </div>
                </b-list-group-item>
            </b-list-group>
        
            <div class="overflow-auto">
                <b-pagination
                    id="pointPaginate"
                    v-model="page"
                    pills
                    :total-rows="rows"
                    :per-page="perPage"
                    first-text="First"
                    prev-text="Prev"
                    next-text="Next"
                    last-text="Last"
                    align="center"
                    aria-controls="point-list"
                />
            </div>
        </template>

        <template v-else>
            {{ $t('account.nocontent.point') }}
        </template>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import moment from 'moment';

export default {
    data: () => ({
        totalPoint: '',
        pointList: [],
        page: 1,
        rows: 1,
        perPage: 10,
    }),
    computed: {
        ...mapGetters(['nickname']),
    },
    watch: {
        page() {
            this.fetchPointHistory(this.page);
        },
    },
    created() {
        this.fetchPointHistory(this.page);
    },
    methods: {
        async fetchPointHistory(page) {
            const response = await this.$axios({
                method: 'get',
                url: `/point`,
                headers: {
                    nickname : this.nickname,
                },
                params: {
                    page: page || 1,
                },
            }).then((res) => {
                this.totalPoint = res.data.data.totalPoint;
                this.pointList = res.data.data.pList.content;
                this.rows = res.data.data.pList.totalElements;
                this.perPage = res.data.data.pList.size;
            }).catch((error) => {
            });
        },
        getPointSign(change){
            if(change >= 0) return '+'+change;
            else return change;
        },
        getFormatDate(date) {
            return moment(new Date(date)).format("YYYY.MM.DD HH:mm:ss");
        },
    },
};
</script>

<style scoped>
#pointPaginate {
    margin-top: 1rem;
}
.plus-point {
    color: cadetblue;
}
.minus-point {
    color: tomato;
}
h3 {
    text-align: center;
}
.item {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap; 
}
.item:nth-child(1) { flex: 1 1 100px; }
.item:nth-child(2) { flex: 2 1 100px; }
.item:nth-child(3) { flex: 1 1 100px; }
</style>
