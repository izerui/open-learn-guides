<template>
    <el-container>
        <el-table :data="workList" stripe>
            <!--<el-table-column prop="CourseName" label="课程名称"/>-->
            <el-table-column prop="ExerciseName" label="作业名称"/>
            <el-table-column prop="ExerciseName" label="起止时间">
                <template slot-scope="scope">
                    <div>
                        {{scope.row.StartDate}} 至 {{scope.row.EndDate}}
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="ExerciseName" label="提交次数" width="100px">
                <template slot-scope="scope">
                    <span>{{scope.row.SubmitCount}} / {{scope.row.MaxTimesOfTrying}} 次</span>
                </template>
            </el-table-column>
            <el-table-column prop="MaxScore" label="最高分" width="100px"/>
            <el-table-column prop="MaxScore" label="操作" width="250px">
                <template slot-scope="scope">
                    <div>
                        <el-button size="mini" v-if="scope.row.state === '2' || scope.row.state === '3'" @click="gotoDoWork(scope.row)">继续作答</el-button>
                        <el-button size="mini" v-else-if="scope.row.state === '4'">批阅中</el-button>
                        <el-button size="mini" v-else-if="scope.row.state === '5'" @click="gotoDoWork(scope.row)">重做</el-button>
                        <el-button size="mini" v-else @click="gotoDoWork(scope.row)">做作业</el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>
        <TestPaper :show.sync="paperShow" :paper-json="paperJson" @refresh="getMyWork"></TestPaper>
    </el-container>
</template>

<script>

    import TestPaper from "./TestPaper";
    import ElPager from "element-ui/packages/pagination/src/pager";
    export default {
        name: 'MyWork',
        components: {ElPager, TestPaper},
        data() {
            return {
                workList: [],
                paperShow: false,
                paperJson: {},
            };
        },
        created() {
            this.getMyWork();
        },
        methods: {
            async getMyWork() {
                const result = await this.$get("/myWork");
                var alist = new Array();
                result.forEach(s => {
                    s.Data.forEach(d => {
                        d.Count = s.Count;
                        d.CourseName = s.CourseName;
                        d.url = s.url;
                        alist.push(d);
                    });
                })
                this.workList = alist;
                console.log("作业列表...", this.workList);
            },
            async gotoDoWork(row) {
                console.log("点击行", row);
                const url = `http://learn.open.com.cn/StudentCenter/OnLineJob/Redirect?mode=1&courseExerciseID=${row.CourseExerciseID}&submitCount=${row.SubmitCount}&studentHomeworkId=${row.studentHomeworkId}`;
                console.log('跳转试卷页面', url);
                const key = await this.$get("/getTestPagerKey", {url: encodeURI(url)});
                const testPaperUrl = `http://learn.open.com.cn/StudentCenter/OnlineJob/DoHomework?courseExerciseId=${row.CourseExerciseID}&key=${key}&studentHomeworkId=${row.studentHomeworkId}&_=${(new Date()).getTime()}`;
                console.log("请求试卷key", testPaperUrl);
                this.paperJson = await this.$get("/getTestPaper", {url: encodeURI(testPaperUrl)});
                console.log("获取试卷", this.paperJson);
                this.paperShow = true;
                // window.open(url,"_blank");
            }
        }
    };
</script>
