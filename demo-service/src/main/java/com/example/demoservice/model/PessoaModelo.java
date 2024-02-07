package com.example.demoservice.model;

import com.maphb.annotations.Column;
import com.maphb.annotations.RowKey;
import com.maphb.annotations.Table;

//public class ExampleModel {
//
//    private String attributeExampleOne;
//
//    private String attributeExampleTwo;
//
//    private String attributeExampleThree;
//
//    private String attributeExampleFour;
//
//
//    public String getAttributeExampleOne() {
//        return attributeExampleOne;
//    }
//
//    public void setAttributeExampleOne(String attributeExampleOne) {
//        this.attributeExampleOne = attributeExampleOne;
//    }
//
//    public String getAttributeExampleTwo() {
//        return attributeExampleTwo;
//    }
//
//    public void setAttributeExampleTwo(String attributeExampleTwo) {
//        this.attributeExampleTwo = attributeExampleTwo;
//    }
//
//    public String getAttributeExampleThree() {
//        return attributeExampleThree;
//    }
//
//    public void setAttributeExampleThree(String attributeExampleThree) {
//        this.attributeExampleThree = attributeExampleThree;
//    }
//
//    public String getAttributeExampleFour() {
//        return attributeExampleFour;
//    }
//
//    public void setAttributeExampleFour(String attributeExampleFour) {
//        this.attributeExampleFour = attributeExampleFour;
//    }
//}




@Table(name = "tb_pessoa_modelo", columnFamilies = {"dados_basicos"})
public class PessoaModelo {

    @RowKey(keys = {"nome"}, delimiter = "#")
    private String rowKey;

    @Column(qualifier = "nome", family = "dados_basicos")
    private String nome;

    @Column(qualifier = "endereco", family = "dados_basicos")
    private String endereco;

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}



