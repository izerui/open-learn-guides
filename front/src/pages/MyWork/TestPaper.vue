<template>
    <el-dialog :visible.sync="dialogShow" :title="titleLabel" :close-on-click-modal="false">
        <el-collapse>
            <el-collapse-item :title="sec.Title" :name="sec.Identifier"
                              v-for="sec in sections">
                <el-card v-for="(qes,index) in sec.items">
                    <div slot="header" class="clearfix">
                        <span>{{index+1}}. </span>
                        <span>{{qes.I2}}</span>
                        <span style="float: right;">{{index+1}}. {{qes.ans}}</span>
                    </div>
                    <div v-for="ch in qes.Choices" class="text item">
                        <span>{{ch.I1}}. </span>
                        {{ch.I2}}
                    </div>
                </el-card>
            </el-collapse-item>
        </el-collapse>
    </el-dialog>
</template>

<script>
    import jsonViewer from 'vue-json-viewer';

    export default {
        name: "TestPaper",
        components: {
            jsonViewer
        },
        props: {
            show: {
                type: Boolean,
                default: false
            },
            paperJson: {
                type: Object,
                required: false,
            }
        },
        data() {
            return {
                itemBankId: '',
                sections: [],
                doneAnswer: false
            }
        },
        computed: {
            dialogShow: {
                get() {
                    if (this.show) {
                        this.doneAnswer = false;
                        this.wrapSections();
                        this.getAnswers();
                    }
                    return this.show;
                },
                set(val) {
                    this.$emit('update:show', val);
                }
            },
            titleLabel(){
                if(this.doneAnswer){
                    return this.paperJson.ExerciseName;
                }else{
                    return this.paperJson.ExerciseName + "   -  (正在获取答案)...";
                }
            }
        },
        methods: {
            wrapSections() {
                this.itemBankId = this.paperJson.TestPaperContent.Model.P3;

                this.sections = this.paperJson.TestPaperContent.Sections;

                const questions = this.paperJson.TestPaperContent.Items;

                this.sections.forEach(s => {
                    if (!s.items) {
                        s.items = [];
                    }
                    s.ItemID.forEach(id => {
                        questions.forEach(ques => {
                            if (ques.I1 === id) {
                                s.items.push(ques);
                            }
                        })
                    })
                });
                console.log("包装后", this.sections);
            },
            async getAnswers() {
                for (var i = 0; i < this.sections.length; i++) {
                    const s = this.sections[i];
                    for (var j = 0; j < s.items.length; j++) {
                        const q = s.items[j];
                        const res = await this.$post("/getAnswer", {
                            itemBankId: this.itemBankId,
                            questionId: q.I1
                        });
                        res.ans = "答案: ";
                        res.Choices.forEach(ch => {
                            if (ch.IsCorrect) {
                                res.ans += ch.I1;
                                res.ans += "、";
                            }
                        })
                        s.items[j] = res;
                        // s.items.splice(j, 1, res);
                        // q.Choices = res.Choices;
                    }
                }
                this.doneAnswer = true;
                console.log("答案", this.sections);
            }
        }
    }
</script>