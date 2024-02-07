package com.example.demoservice.repository;

import com.example.demoservice.model.ExampleModel;
import com.example.demoservice.model.PessoaModelo;
import com.maphb.manager.GenericRepository;

public class PessoaModeloRepository extends GenericRepository<PessoaModelo> {

    public PessoaModeloRepository(Class<PessoaModelo> clazz) {
        super(clazz);
    }
}



