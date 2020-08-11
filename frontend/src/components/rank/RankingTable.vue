<template>
    <div>
        <template v-if="tableType==='user'">
            <div>
                <b-card no-body>
                    <b-tabs 
                        active-nav-item-class="bg-dark"
                        pills 
                        card
                    >
                        <b-tab
                            title="랭킹"
                            disabled
                        >
                            <template
                                v-slot:title
                            >
                                <strong class="tab-ranking">랭킹</strong> <i class="far fa-hand-point-right" />
                            </template>
                        </b-tab>
                        <b-tab 
                            title="고민" 
                            active
                        >
                            <UserTable
                                :my-sort="2"
                            />
                        </b-tab>
                        <b-tab 
                            title="답변"
                        >
                            <UserTable
                                :my-sort="3"
                            />
                        </b-tab>
                        <b-tab 
                            title="포인트"
                        >
                            <UserTable
                                :my-sort="4"
                            />
                        </b-tab>
                        <b-tab 
                            title="레벨"
                        >
                            <UserTable
                                :my-sort="5"
                            />
                        </b-tab>
                    </b-tabs>
                </b-card>
            </div>
        </template>
        <template v-else-if="tableType==='post'">
            <div>
                <b-card no-body>
                    <b-tabs 
                        active-nav-item-class="bg-dark"
                        pills 
                        card
                    >   
                        <b-tab
                            title="랭킹"
                            disabled
                        >
                            <template
                                v-slot:title
                            >
                                <strong class="tab-ranking">랭킹</strong> <i class="far fa-hand-point-right" />
                            </template>
                        </b-tab>
                        <b-tab 
                            title="추천수" 
                            active
                        >
                            <PostTable
                                :my-sort="6"
                            />
                        </b-tab>
                        <b-tab title="조회수">
                            <PostTable
                                :my-sort="7"
                            />
                        </b-tab>
                        <b-tab title="답변수">
                            <PostTable
                                :my-sort="8"
                            />
                        </b-tab>
                    </b-tabs>
                </b-card>
            </div>
        </template>
        <template v-else>
            <div>
                <b-card no-body>
                    <b-tabs 
                        active-nav-item-class="bg-dark"
                        pills 
                        card
                    >
                        <b-tab
                            title="랭킹"
                            disabled
                        >
                            <template
                                v-slot:title
                            >
                                <strong class="tab-ranking">랭킹</strong> <i class="far fa-hand-point-right" />
                            </template>
                        </b-tab>
                        <b-tab 
                            title="추천수" 
                            active
                        >
                            <ReplyTable />
                        </b-tab>
                    </b-tabs>
                </b-card>
            </div>
        </template>
    </div>
</template>

<script>
import UserTable from '@/components/rank/UserTable.vue';
import PostTable from '@/components/rank/PostTable.vue';
import ReplyTable from '@/components/rank/ReplyTable.vue';
export default {
    components:{
        UserTable,
        PostTable,
        ReplyTable
    },
    props:{
        tableType:{
            type: String,
            required: true,
        }
    }
};
</script>

<style>
.tab-ranking{
    font-size: 20px;
}
</style>