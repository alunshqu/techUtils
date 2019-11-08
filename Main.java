package com.alun.toolutils;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static String specialStr = "[]{}:,";

    public static void print(Node root) {
        if (null == root) {
            return;
        } else {
            if ("".equalsIgnoreCase(root.getValue()) || specialStr.indexOf(root.getValue()) == -1) {
                System.out.print("\"" + root.getValue().trim() + "\"");
            } else {
                System.out.print(root.getValue());
            }
        }

        if (CollectionUtils.isEmpty(root.getChildren())) {
            return;
        } else {
            for (Node node : root.getChildren()) {
                print(node);
            }
        }
    }

    public static void transferToJson(String str) {
        Node currency = null;
        Node root = null;
        boolean shouldCreateNode = true;
        boolean currencyIsEqual = false;
        boolean currencyIsKey = true;
        boolean currencyIsValue = false;
        for (char c : str.toCharArray()) {
            if ('[' == c || '{' == c) {

                if (currencyIsEqual) {
                    currency.setValue(String.valueOf(c));
                } else {
                    Node temp = new Node();
                    temp.setValue(String.valueOf(c));
                    if (null == root) {
                        currency = temp;
                        root = currency;
                    } else {
                        currency.getChildren().add(temp);
                        temp.setParent(currency);
                        currency = temp;
                    }
                }

                shouldCreateNode = true;
                currencyIsEqual = false;
                currencyIsValue = false;
            } else if (']' == c || '}' == c) {
                Node temp = new Node();
                temp.setValue(String.valueOf(c));
                currency.getChildren().add(temp);
                temp.setParent(currency);
                currency = temp;
                shouldCreateNode = true;
                currencyIsEqual = false;
                currencyIsValue = false;
            } else if ('=' == c) {
                if (currencyIsValue) {
                    currency.setValue(currency.getValue() + c);
                    continue;
                }

                currencyIsValue = true;

                Node temp = new Node();
                temp.setValue(":");
                currency.getChildren().add(temp);
                temp.setParent(currency);

                temp = new Node();
                temp.setValue("");
                currency.getChildren().add(temp);
                temp.setParent(currency);
                currency = temp;
                shouldCreateNode = false;
                currencyIsEqual = true;
            } else if (',' == c) {
                Node temp = new Node();
                temp.setValue(String.valueOf(c));
                currency.getChildren().add(temp);
                temp.setParent(currency);
                currency = currency.getParent().getParent();
                shouldCreateNode = true;
                currencyIsEqual = false;
                currencyIsValue = false;
            } else {
                if (shouldCreateNode) {
                    shouldCreateNode = false;
                    Node temp = new Node();
                    temp.setValue(String.valueOf(c));
                    currency.getChildren().add(temp);
                    temp.setParent(currency);
                    currency = temp;
                } else {
                    currency.setValue(currency.getValue() + c);
                }
                currencyIsEqual = false;
            }
        }
        print(root);
    }


    public static void main(String[] args) {
        String str = "[Demo(key=value, list= [123, 456])]";
        transferToJson(initOrigStr(str));
    }

    public static String initOrigStr(String str) {
        return str.replaceAll("\\w+\\(", "{")
                .replaceAll("\\)", "}")
                .replaceAll(", ", ",")
                .replaceAll(" \\[", "[")
                .replaceAll(" \\{", "{")
                .replaceAll("} ", "}");
    }
}


@Data
@ToString
class Node {
    private Node parent;
    private List<Node> children = new ArrayList<>();
    private String value;
}
