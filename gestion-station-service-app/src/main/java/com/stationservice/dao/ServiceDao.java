package com.stationservice.dao;

import com.stationservice.Models.Service;
import com.stationservice.Models.ServiceItem;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface ServiceDao {

    @SqlQuery("SELECT num_serv AS numServ, service AS nomService, prix_service AS prixService FROM service ORDER BY service ASC")
    @RegisterBeanMapper(Service.class)
    List<Service> findAll();

    @SqlQuery("SELECT num_serv AS numServ, service AS nomService, prix_service AS prixService FROM service ORDER BY service ASC")
    @RegisterBeanMapper(ServiceItem.class)
    List<ServiceItem> findAllItems();

    @SqlUpdate("INSERT INTO service (num_serv, service, prix_service) VALUES (:numServ, :nomService, :prixService)")
    boolean insert(@BindBean Service service);

    @SqlUpdate("UPDATE service SET service = :nomService, prix_service = :prixService WHERE num_serv = :numServ")
    boolean update(@BindBean Service service);

    @SqlUpdate("DELETE FROM service WHERE num_serv = :numServ")
    boolean delete(@Bind("numServ") String numServ);
}