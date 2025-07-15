// priority_model.java
package com.tasktracker.model;

public class Priority {
    private int id;
    private String name;

    public Priority() {
    }

    public Priority(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Priority{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}