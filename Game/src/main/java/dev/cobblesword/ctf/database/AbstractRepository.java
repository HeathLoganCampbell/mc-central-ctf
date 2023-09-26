package dev.cobblesword.ctf.database;

import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filters;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//This is a abstract class with a generic used to access an objects data
public abstract class AbstractRepository<T> {

  //Datastore, which acts an access point for all database operations
  @Getter
  private Datastore datastore;

  public AbstractRepository(@NotNull Datastore datastore) {
    this.datastore = datastore;
  }

  //Method to save T-type object in a database, we return this so that if we autogenerate
  // ids it will return it
  public T save(T entity) {
    //Actually saves it into the database
    entity = datastore.save(entity);
    return entity;
  }

  //Returns a T type object based on the id being equal to a string
  public T get(@NotNull String id) {
    return datastore.find(getEntityClass()).filter(Filters.eq("_id", id)).first();
  }

  //Returns a T type object based on the id being equal to UUID,
  // which is useful in the context of minecraft
  public T get(@NotNull UUID uuid) {
    return datastore.find(getEntityClass()).filter(Filters.eq("_id", uuid)).first();

  }

  //Returns a list of T type objects based on fields being equal to value
  public List<T> findAll(@NotNull String field, @NotNull String value) {
    return datastore.find(getEntityClass()).filter(Filters.eq(field, value)).stream().collect(Collectors.toList());
  }

  //Finds the first T object that has a certain field
  public T findFirst(@NotNull String field, @NotNull String value) {
    return findAll(field, value).get(0);
  }

  //Deletes object with certain id
  public void delete(@NotNull String id) {
    Query<T> query = datastore.find(getEntityClass()).filter(Filters.eq("_id", id));
    T first = query.first();

    if (first != null) {
      datastore.delete(first);
    }
  }
 
  //Deletes object based on T
  public void delete(@NotNull T ob) {
    datastore.delete(ob);
  }

  //Finds last of all objects
  public T findLast() {
    List<T> results = datastore.find(getEntityClass()).stream().collect(Collectors.toList());

    return results.get(results.size() - 1);
  }

  // Return list of all Ts
  public List<T> findAll() {
    return datastore.find(getEntityClass()).stream().collect(Collectors.toList());
  }


  /**
   * Class we are trying to use for this DAO
   *
   * @return Class of this type
   */
  protected abstract Class<T> getEntityClass();
}