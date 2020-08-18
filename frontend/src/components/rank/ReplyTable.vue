<template>
    <div>
        <template v-if="list && list.length">
            <table class="table">
                <thead class="thead-light">
                    <tr>
                        <th>
                            {{ $t('ranking.table.rank') }}
                        </th>
                        <th>
                            {{ $t('ranking.table.cNumber') }}
                        </th>
                        <th>
                            {{ $t('ranking.table.writer') }}
                        </th>
                        <th>
                            {{ $t('ranking.table.like') }}
                            <i
                                class="fas fa-long-arrow-alt-down"
                            />
                        </th>
                        <th>
                            {{ $t('ranking.table.date') }}
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr 
                        v-for="(item,index) in list"
                        :key="item.nickname"
                    >
                        <td>{{ index+1 }}</td>
                        <td>
                            <router-link
                                :to="{name: 'CounselDetail', params: {no: item.postNo}}"
                            >
                                {{ item.postNo }}
                            </router-link>
                        </td>
                        <td>{{ item.writer }}</td>
                        <td>{{ item.likeCount }}</td>
                        <td>{{ item.writeDate }}</td>
                    </tr>
                </tbody>
            </table>
        </template>
    </div>
</template>

<script>
export default {
    data() {
        return {
            list:[]
        };
    },
    created() {
        this.$axios({
            url:"/rank/reply",
            method:"GET",
        }).then((res)=>{
            if(res.data.status==="S-200"){
                let formatDate = function (date) {
                    let d = new Date(date),
                        month = '' + (d.getMonth() + 1),
                        day = '' + d.getDate(),
                        year = d.getFullYear();
                    if (month.length < 2) 
                        month = '0' + month;
                    if (day.length < 2) 
                        day = '0' + day;
                    return [month, day].join('-');
                };
                this.list = res.data.data.map((e)=>{
                    e.writeDate = formatDate(e.writeDate);
                    return e;
                });
            }
        }).catch(e=>{
            console.log(e);
        });
    },
    methods: {
    },
};
</script>

<style>

</style>